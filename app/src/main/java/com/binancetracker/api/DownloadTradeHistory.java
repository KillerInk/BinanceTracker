package com.binancetracker.api;


import com.binance.api.client.BinanceApiClientFactory;
import com.binancetracker.api.runnable.DownloadLastTradeHistoryRunner;
import com.binancetracker.api.runnable.DownloadTradeFullHistoryRunner;
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

    public DownloadTradeHistory(BinanceApiClientFactory clientFactory)
    {
        downloadTradeFullHistoryRunner = new DownloadTradeFullHistoryRunner(clientFactory);
        downloadLastTradeHistoryRunner = new DownloadLastTradeHistoryRunner(clientFactory);
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
