package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.utils.MyTime;
import com.binancetracker.utils.Settings;
import java.util.List;

public class DownloadLatestDayHistoryForAllPairsRunner extends DownloadFullDayHistoryForAllPairsRunner {

    private final String TAG = DownloadLatestDayHistoryForAllPairsRunner.class.getSimpleName();
    public DownloadLatestDayHistoryForAllPairsRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase, Settings settings) {
        super(clientFactory, singletonDataBase, settings);
    }

    @Override
    public void run() {
        long lastentrydate = singletonDataBase.binanceDatabase.candelStickDayDao().getLatestDate();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        List<String> assets = getPairsToDownload();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-30);
        Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        Log.d(TAG,"download start priceHistory for " + assets.size());
        getCandlestickRangeForAssets(client, assets, endtime.getTime(), starttime.getTime());

        Log.d(TAG,"download priceHistory done ");
    }
}
