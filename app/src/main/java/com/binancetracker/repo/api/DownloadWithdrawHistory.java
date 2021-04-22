package com.binancetracker.repo.api;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.Download30DaysWithdrawHistoryRunner;
import com.binancetracker.repo.api.runnable.DownloadWithdrawFullHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;

public class DownloadWithdrawHistory {
    private DownloadWithdrawFullHistory downloadWithdrawFullHistory;
    private Download30DaysWithdrawHistoryRunner download30DaysWithdrawHistoryRunner;

    public DownloadWithdrawHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        downloadWithdrawFullHistory = new DownloadWithdrawFullHistory(clientFactory,singletonDataBase);
        download30DaysWithdrawHistoryRunner = new Download30DaysWithdrawHistoryRunner(clientFactory,singletonDataBase);
    }

    public void downloadFullHistory()
    {
        RestExecuter.addTask(downloadWithdrawFullHistory);
    }

    public void downloadLast30days(boolean threaded)
    {
        if (threaded)
            RestExecuter.addTask(download30DaysWithdrawHistoryRunner);
        else download30DaysWithdrawHistoryRunner.run();
    }

    public void setMessageEvent(ClientFactoryRunner.MessageEvent messageEvent)
    {
        download30DaysWithdrawHistoryRunner.setMessageEventListner(messageEvent);
        downloadWithdrawFullHistory.setMessageEventListner(messageEvent);
    }
}
