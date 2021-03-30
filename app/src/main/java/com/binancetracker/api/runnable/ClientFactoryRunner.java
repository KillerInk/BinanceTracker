package com.binancetracker.api.runnable;

import com.binance.api.client.BinanceApiClientFactory;

public abstract class ClientFactoryRunner implements Runnable {
    protected BinanceApiClientFactory clientFactory;

    public ClientFactoryRunner(BinanceApiClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
    }
}
