package com.binancetracker.api;

import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.api.runnable.DownloadFullDayHistoryForAllPairsRunner;
import com.binancetracker.api.runnable.DownloadLatestDayHistoryForAllPairsRunner;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.thread.RestExecuter;
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
