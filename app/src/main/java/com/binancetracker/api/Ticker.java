package com.binancetracker.api;

import android.text.TextUtils;

import com.binance.api.client.api.BinanceApiWebSocketClient;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;

import java.io.Closeable;
import java.io.IOException;

public class Ticker {

    public interface PriceChangedEvent
    {
        void onPriceChanged(String symbol, double price, String percentChange);
    }

    private BinanceSpotApiClientFactory clientFactory;
    private String listenKey;
    private Closeable dataStream;
    private String marketsToListen;
    private PriceChangedEvent priceChangedEvent;

    public Ticker(BinanceSpotApiClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
    }

    public void setMarketsToListen(String marketsToListen)
    {
        this.marketsToListen = marketsToListen.toLowerCase();
    }

    public void setPriceChangedEvent(PriceChangedEvent priceChangedEvent) {
        this.priceChangedEvent = priceChangedEvent;
    }

    public void start()
    {
        BinanceApiWebSocketClient client = clientFactory.newWebSocketClient();

        if (marketsToListen != null && !TextUtils.isEmpty(marketsToListen))
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dataStream = client.onTickerEvent(marketsToListen,event -> {
                        if (priceChangedEvent != null)
                            priceChangedEvent.onPriceChanged(event.getSymbol(),Double.parseDouble(event.getCurrentDaysClosePrice()),event.getPriceChangePercent());
                    });
                }
            }).start();

    }

    public void stop()
    {
        if (dataStream != null) {
            try {
                dataStream.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        dataStream = null;
    }
}
