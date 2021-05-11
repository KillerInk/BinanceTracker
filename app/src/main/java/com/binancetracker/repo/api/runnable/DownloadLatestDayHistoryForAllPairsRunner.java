package com.binancetracker.repo.api.runnable;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.MyTime;
import com.binancetracker.utils.Settings;

import java.util.List;

public class DownloadLatestDayHistoryForAllPairsRunner extends DownloadFullDayHistoryForAllPairsRunner {

    private final String TAG = DownloadLatestDayHistoryForAllPairsRunner.class.getSimpleName();
    public DownloadLatestDayHistoryForAllPairsRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase, Settings settings) {
        super(clientFactory, singletonDataBase, settings);
    }

    @Override
    public void processRun() {
        long lastentrydate = singletonDataBase.binanceDatabase.candelStickDayDao().getLatestDate();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        List<String> assets = getPairsToDownload();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(lastentrydate);
        Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        Log.d(TAG,"download start priceHistory for " + assets.size());
        getCandlestickRangeForAssets(client, assets, null, null,100);

        Log.d(TAG,"download priceHistory done ");
    }
}
