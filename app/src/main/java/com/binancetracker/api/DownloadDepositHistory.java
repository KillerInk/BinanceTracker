package com.binancetracker.api;

import com.binance.api.client.BinanceApiClientFactory;
import com.binancetracker.api.runnable.Download30DaysDepositHistoryRunner;
import com.binancetracker.api.runnable.DownloadDepositFullHistoryRunner;
import com.binancetracker.thread.RestExecuter;


public class DownloadDepositHistory
{
    private DownloadDepositFullHistoryRunner fullHistoryRunner;
    private Download30DaysDepositHistoryRunner d30daysHistorRunner;

    public DownloadDepositHistory(BinanceApiClientFactory clientFactory)
    {
        fullHistoryRunner = new DownloadDepositFullHistoryRunner(clientFactory);
        d30daysHistorRunner = new Download30DaysDepositHistoryRunner(clientFactory);
    }

    public void downloadFullHistory()
    {
        RestExecuter.addTask(fullHistoryRunner);
    }

    public void downloadLast30days(boolean threaded)
    {
        if (threaded)
            RestExecuter.addTask(d30daysHistorRunner);
        else d30daysHistorRunner.run();
    }
}
