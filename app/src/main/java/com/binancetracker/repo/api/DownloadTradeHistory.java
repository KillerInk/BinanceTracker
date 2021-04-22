package com.binancetracker.repo.api;


import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.DownloadLastTradeHistoryRunner;
import com.binancetracker.repo.api.runnable.DownloadTradeFullHistoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;

public class DownloadTradeHistory extends DownloadHistory {

    private DownloadTradeFullHistoryRunner downloadTradeFullHistoryRunner;
    private DownloadLastTradeHistoryRunner downloadLastTradeHistoryRunner;

    public DownloadTradeHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        downloadTradeFullHistoryRunner = new DownloadTradeFullHistoryRunner(clientFactory,singletonDataBase);
        downloadLastTradeHistoryRunner = new DownloadLastTradeHistoryRunner(clientFactory,singletonDataBase);
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

    @Override
    public void setMessageEventListner(ClientFactoryRunner.MessageEvent messageEventListner) {
        downloadTradeFullHistoryRunner.setMessageEventListner(messageEventListner);
        downloadLastTradeHistoryRunner.setMessageEventListner(messageEventListner);
    }
}
