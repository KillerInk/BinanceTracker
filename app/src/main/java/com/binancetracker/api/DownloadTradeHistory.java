package com.binancetracker.api;


import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.api.runnable.DownloadLastTradeHistoryRunner;
import com.binancetracker.api.runnable.DownloadTradeFullHistoryRunner;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.thread.RestExecuter;

public class DownloadTradeHistory {

    public interface TradeHistoryEvent
    {
        void onSyncStart(int max_markets);
        void onSyncUpdate(int currentmarket);
        void onSyncEnd();
    }
    private DownloadTradeFullHistoryRunner downloadTradeFullHistoryRunner;
    private DownloadLastTradeHistoryRunner downloadLastTradeHistoryRunner;

    public DownloadTradeHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        downloadTradeFullHistoryRunner = new DownloadTradeFullHistoryRunner(clientFactory,singletonDataBase);
        downloadLastTradeHistoryRunner = new DownloadLastTradeHistoryRunner(clientFactory,singletonDataBase);
    }

    public void setHistoryEvent(TradeHistoryEvent historyEvent) {
        downloadTradeFullHistoryRunner.setTradeHistoryEventListner(historyEvent);
        downloadLastTradeHistoryRunner.setTradeHistoryEventListner(historyEvent);
    }

    public void getFullHistory()
    {
        RestExecuter.addTask(downloadTradeFullHistoryRunner);
    }

    public void updateHistoryTrades(boolean threaded)
    {
        if (threaded)
            RestExecuter.addTask(downloadLastTradeHistoryRunner);
        else downloadLastTradeHistoryRunner.run();
    }

}
