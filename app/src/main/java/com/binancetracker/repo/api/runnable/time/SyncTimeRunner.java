package com.binancetracker.repo.api.runnable.time;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binance.api.client.impl.BinanceApiServiceGenerator;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;

public class SyncTimeRunner extends ClientFactoryRunner<BinanceSpotApiClientFactory> {
    private static final String TAG = SyncTimeRunner.class.getSimpleName();

    public SyncTimeRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void processRun() {
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        long servertime = client.getServerTime();
        long dif = System.currentTimeMillis() -servertime;
        Log.d(TAG, "TimeDifference " + dif+"ms");
        BinanceApiServiceGenerator.setTimeDifferenceToServer(dif);
    }
}
