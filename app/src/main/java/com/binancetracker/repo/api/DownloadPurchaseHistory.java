package com.binancetracker.repo.api;

import com.binance.api.client.factory.BinanceSavingApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.DownloadSavingPurchaseHistoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;

public class DownloadPurchaseHistory extends DownloadHistory {

    private DownloadSavingPurchaseHistoryRunner downloadSavingPurchaseHistoryRunner;

    public DownloadPurchaseHistory(BinanceSavingApiClientFactory factory, SingletonDataBase singletonDataBase)
    {
        downloadSavingPurchaseHistoryRunner = new DownloadSavingPurchaseHistoryRunner(factory,singletonDataBase);
    }

    @Override
    public void setMessageEventListner(ClientFactoryRunner.MessageEvent messageEventListner) {
        downloadSavingPurchaseHistoryRunner.setMessageEventListner(messageEventListner);
    }

    public void downloadPurchaseHistory()
    {
        RestExecuter.addTask(downloadSavingPurchaseHistoryRunner);
    }
}
