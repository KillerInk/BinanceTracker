package com.binancetracker.api;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.api.runnable.Download30DaysWithdrawHistoryRunner;
import com.binancetracker.api.runnable.DownloadWithdrawFullHistory;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.thread.RestExecuter;

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
}
