package com.binancetracker.repo.api;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.DownloadFullDayHistoryForAllPairsRunner;
import com.binancetracker.repo.api.runnable.DownloadLatestDayHistoryForAllPairsRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;
import com.binancetracker.utils.Settings;

public class DownloadCandleStickHistory {
    private DownloadLatestDayHistoryForAllPairsRunner downloadLatestDayHistoryForAllPairsRunner;
    private DownloadFullDayHistoryForAllPairsRunner downloadFullDayHistoryForAllPairsRunner;

    public DownloadCandleStickHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase, Settings settings)
    {
        downloadFullDayHistoryForAllPairsRunner = new DownloadFullDayHistoryForAllPairsRunner(clientFactory,singletonDataBase,settings);
        downloadLatestDayHistoryForAllPairsRunner = new DownloadLatestDayHistoryForAllPairsRunner(clientFactory,singletonDataBase,settings);
    }

    public void downloadFullHistory()
    {
        RestExecuter.addTask(downloadFullDayHistoryForAllPairsRunner);
    }

    public void downloadLatestHistory(boolean threaded)
    {
        if (threaded)
            RestExecuter.addTask(downloadLatestDayHistoryForAllPairsRunner);
        else
            downloadLatestDayHistoryForAllPairsRunner.run();
    }
}
