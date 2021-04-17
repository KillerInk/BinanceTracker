package com.binancetracker.api.runnable;

import com.binance.api.client.BinanceApiClientFactory;
import com.binancetracker.room.SingletonDataBase;

public abstract class ClientFactoryRunner implements Runnable {
    protected BinanceApiClientFactory clientFactory;
    protected SingletonDataBase singletonDataBase;

    public ClientFactoryRunner(BinanceApiClientFactory clientFactory,SingletonDataBase singletonDataBase)
    {
        this.clientFactory = clientFactory;
        this.singletonDataBase = singletonDataBase;
    }
}
