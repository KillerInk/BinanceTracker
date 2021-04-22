package com.binancetracker.repo.api;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.Download30DaysDepositHistoryRunner;
import com.binancetracker.repo.api.runnable.DownloadDepositFullHistoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;


public class DownloadDepositHistory extends DownloadHistory
{
    private DownloadDepositFullHistoryRunner fullHistoryRunner;
    private Download30DaysDepositHistoryRunner d30daysHistorRunner;

    public DownloadDepositHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        fullHistoryRunner = new DownloadDepositFullHistoryRunner(clientFactory,singletonDataBase);
        d30daysHistorRunner = new Download30DaysDepositHistoryRunner(clientFactory,singletonDataBase);
    }

    @Override
    public void setMessageEventListner(ClientFactoryRunner.MessageEvent messageEventListner)
    {
        fullHistoryRunner.setMessageEventListner(messageEventListner);
        d30daysHistorRunner.setMessageEventListner(messageEventListner);
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
