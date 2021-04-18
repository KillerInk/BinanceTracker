package com.binancetracker.api.runnable;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.room.SingletonDataBase;

public abstract class ClientFactoryRunner implements Runnable {
    protected BinanceSpotApiClientFactory clientFactory;
    protected SingletonDataBase singletonDataBase;

    public ClientFactoryRunner(BinanceSpotApiClientFactory clientFactory,SingletonDataBase singletonDataBase)
    {
        this.clientFactory = clientFactory;
        this.singletonDataBase = singletonDataBase;
    }
}
