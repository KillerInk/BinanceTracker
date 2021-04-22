package com.binancetracker.repo.api;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.FuturesTransactionHistory;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.DownloadFuturesTransactionHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;

public class DownloadFuturesHistory {
    private DownloadFuturesTransactionHistory downloadFuturesTransactionHistory;

    public DownloadFuturesHistory(BinanceSpotApiClientFactory binanceApiSpotRestClient, SingletonDataBase singletonDataBase)
    {
        downloadFuturesTransactionHistory = new DownloadFuturesTransactionHistory(binanceApiSpotRestClient,singletonDataBase);
    }

    public void setMessageEventListner(ClientFactoryRunner.MessageEvent tradeHistoryEventListner) {
        downloadFuturesTransactionHistory.setMessageEventListner(tradeHistoryEventListner);
    }

    public void downloadFullHistory()
    {
        RestExecuter.addTask(downloadFuturesTransactionHistory);
    }
}
