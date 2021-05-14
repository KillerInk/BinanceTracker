package com.binancetracker.repo.api.runnable.market;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.exception.BinanceApiException;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.JsonToDBConverter;
import com.binancetracker.repo.api.runnable.account.DownloadDepositFullHistoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.CandleStickEntity;
import com.binancetracker.utils.Settings;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownloadFullDayHistoryForAllPairsRunner extends DownloadDepositFullHistoryRunner {

    private final String TAG = DownloadFullDayHistoryForAllPairsRunner.class.getSimpleName();
    private Settings settings;

    public DownloadFullDayHistoryForAllPairsRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase, Settings settings) {
        super(clientFactory,singletonDataBase);
        this.settings = settings;
    }

    @Override
    public void processRun() {
        singletonDataBase.binanceDatabase.candelStickDayDao().deleteAll();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        List<String> assets = getPairsToDownload();
        fireOnSyncStart(assets.size());
        long endtime = System.currentTimeMillis();
        long starttime = endtime - (days30 * checkyears);
        Log.d(TAG,"download start priceHistory for " + assets.size());
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        getCandlestickRangeForAssets(client, assets, endtime, starttime,1000);

        fireOnSyncEnd();
        Log.d(TAG,"download priceHistory done ");
    }

    protected void getCandlestickRangeForAssets(BinanceApiSpotRestClient client, List<String> assets, Long endtime, Long starttime, int limit) {
        int i = 1;
        try {
            for (String asset : assets)
            {
                fireOnSyncUpdate(i,"download priceHistory for " + asset);
                Log.d(TAG,"download priceHistory for " + asset);
                List<Candlestick> candlestickList = client.getCandlestickBars(asset, CandlestickInterval.DAILY,limit,starttime,endtime);
                Log.d(TAG,"candle count: " + candlestickList.size());
                addListToDb(candlestickList,asset);
                if (candlestickList.size() == 1000)
                {
                    long newstarttime = candlestickList.get(candlestickList.size()-1).getOpenTime();
                    candlestickList = client.getCandlestickBars(asset, CandlestickInterval.DAILY,1000,newstarttime,endtime);
                    Log.d(TAG,"candle count: " + candlestickList.size());
                    addListToDb(candlestickList,asset);
                }
                Log.d(TAG,"download priceHistory for " + asset + " done " + i++ + "/" + assets.size());
            }
        }
        catch (BinanceApiException ex)
        {
            ex.printStackTrace();
        }
    }


    protected List<String> getPairsToDownload()
    {
        List<String> assets = singletonDataBase.appDatabase.assetModelDao().getAllAssets();
        List<String> profitassets = singletonDataBase.appDatabase.profitDao().getAllAssets();
        List<String> pairsToDl = new ArrayList<>();
        String usdt = "USDT";
        String defasset = settings.getDefaultAsset();

        for (String asset : assets)
        {
            if (!asset.equals(usdt) && !asset.equals(defasset))
                pairsToDl.add(asset+usdt);
        }
        for (String asset : profitassets)
        {
            if (!asset.equals(usdt) && !asset.equals(defasset))
                pairsToDl.add(asset+usdt);
        }
        if (!defasset.equals(usdt))
            pairsToDl.add((defasset+usdt));
        Log.d(TAG, "Pairs to dl:" + pairsToDl.toString());
        return pairsToDl;
    }


    private void addListToDb(List<Candlestick> candlesticks, String symbol)
    {
        for (Candlestick candelStick : candlesticks)
        {
            addCandelStickToDb(candelStick,symbol);
        }
    }

    private void addCandelStickToDb(Candlestick candlestick,String symbol)
    {
        CandleStickEntity candleStickEntity = JsonToDBConverter.getCandleStickEntity(candlestick, symbol);
        singletonDataBase.binanceDatabase.candelStickDayDao().insert(candleStickEntity);
    }


}
