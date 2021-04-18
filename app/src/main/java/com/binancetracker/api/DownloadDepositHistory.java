package com.binancetracker.api;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.api.runnable.Download30DaysDepositHistoryRunner;
import com.binancetracker.api.runnable.DownloadDepositFullHistoryRunner;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.thread.RestExecuter;


public class DownloadDepositHistory
{
    private DownloadDepositFullHistoryRunner fullHistoryRunner;
    private Download30DaysDepositHistoryRunner d30daysHistorRunner;

    public DownloadDepositHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        fullHistoryRunner = new DownloadDepositFullHistoryRunner(clientFactory,singletonDataBase);
        d30daysHistorRunner = new Download30DaysDepositHistoryRunner(clientFactory,singletonDataBase);
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
