package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.exception.BinanceApiException;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.utils.Settings;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownloadFullDayHistoryForAllPairsRunner extends DownloadDepositFullHistoryRunner{

    private final String TAG = DownloadFullDayHistoryForAllPairsRunner.class.getSimpleName();

    public DownloadFullDayHistoryForAllPairsRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        List<String> assets = getPairsToDownload();

        long endtime = System.currentTimeMillis();
        long starttime = endtime - (days30 * checkyears);
        Log.d(TAG,"download start priceHistory for " + assets.size());
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        int i = 1;
        try {
            for (String asset : assets)
            {
                Log.d(TAG,"download priceHistory for " + asset);
                List<Candlestick> candlestickList = client.getCandlestickBars(asset, CandlestickInterval.DAILY,1000,starttime,endtime);
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

        Log.d(TAG,"download priceHistory done ");
    }

    private List<String> getPairsToDownload()
    {
        List<String> assets = SingletonDataBase.appDatabase.assetModelDao().getAllAssets();
        List<String> profitassets = SingletonDataBase.appDatabase.profitDao().getAllAssets();
        List<String> pairsToDl = new ArrayList<>();
        String usdt = "USDT";
        String defasset = Settings.getInstance().getDefaultAsset();

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
        CandleStickEntity candleStickEntity = new CandleStickEntity();
        candleStickEntity.close = candlestick.getClose();
        candleStickEntity.closeTime = candlestick.getCloseTime();
        candleStickEntity.high = candlestick.getHigh();
        candleStickEntity.low = candlestick.getLow();
        candleStickEntity.numberOfTrades = candlestick.getNumberOfTrades();
        candleStickEntity.open = candlestick.getOpen();
        candleStickEntity.openTime = candlestick.getOpenTime();
        candleStickEntity.quoteAssetVolume = candlestick.getQuoteAssetVolume();
        candleStickEntity.takerBuyBaseAssetVolume = candlestick.getTakerBuyBaseAssetVolume();
        candleStickEntity.takerBuyQuoteAssetVolume = candlestick.getTakerBuyQuoteAssetVolume();
        candleStickEntity.symbol =symbol;
        candleStickEntity.volume = candlestick.getVolume();
        candleStickEntity.id = (long)(symbol+candlestick.getOpenTime()).hashCode();
        SingletonDataBase.binanceDatabase.candelStickDayDao().insert(candleStickEntity);
    }
}
