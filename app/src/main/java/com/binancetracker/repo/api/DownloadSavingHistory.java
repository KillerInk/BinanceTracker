package com.binancetracker.repo.api;

import com.binance.api.client.factory.BinanceSavingApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.DownloadSavingInterestHistoryRunner;
import com.binancetracker.repo.api.runnable.DownloadSavingPurchaseHistoryRunner;
import com.binancetracker.repo.api.runnable.DownloadSavingRedemptionHistoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;

public class DownloadSavingHistory extends DownloadHistory {

    private DownloadSavingPurchaseHistoryRunner downloadSavingPurchaseHistoryRunner;
    private DownloadSavingInterestHistoryRunner downloadSavingInterestHistoryRunner;
    private DownloadSavingRedemptionHistoryRunner downloadSavingRedemptionHistoryRunner;

    public DownloadSavingHistory(BinanceSavingApiClientFactory factory, SingletonDataBase singletonDataBase)
    {
        downloadSavingPurchaseHistoryRunner = new DownloadSavingPurchaseHistoryRunner(factory,singletonDataBase);
        downloadSavingInterestHistoryRunner = new DownloadSavingInterestHistoryRunner(factory,singletonDataBase);
        downloadSavingRedemptionHistoryRunner = new DownloadSavingRedemptionHistoryRunner(factory,singletonDataBase);
    }

    @Override
    public void setMessageEventListner(ClientFactoryRunner.MessageEvent messageEventListner) {
        downloadSavingPurchaseHistoryRunner.setMessageEventListner(messageEventListner);
        downloadSavingInterestHistoryRunner.setMessageEventListner(messageEventListner);
        downloadSavingRedemptionHistoryRunner.setMessageEventListner(messageEventListner);
    }

    public void downloadPurchaseHistory()
    {
        RestExecuter.addTask(downloadSavingPurchaseHistoryRunner);
    }

    public void downloadInterestHistory()
    {
        RestExecuter.addTask(downloadSavingInterestHistoryRunner);
    }

    public void downloadRedemptionHistory()
    {
        RestExecuter.addTask(downloadSavingRedemptionHistoryRunner);
    }
}
