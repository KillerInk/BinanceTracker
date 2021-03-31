package com.binancetracker.api;

import com.binance.api.client.BinanceApiClientFactory;
import com.binancetracker.api.runnable.Download30DaysWithdrawHistoryRunner;
import com.binancetracker.api.runnable.DownloadWithdrawFullHistory;
import com.binancetracker.thread.RestExecuter;

public class DownloadWithdrawHistory {
    private DownloadWithdrawFullHistory downloadWithdrawFullHistory;
    private Download30DaysWithdrawHistoryRunner download30DaysWithdrawHistoryRunner;

    public DownloadWithdrawHistory(BinanceApiClientFactory clientFactory)
    {
        downloadWithdrawFullHistory = new DownloadWithdrawFullHistory(clientFactory);
        download30DaysWithdrawHistoryRunner = new Download30DaysWithdrawHistoryRunner(clientFactory);
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
