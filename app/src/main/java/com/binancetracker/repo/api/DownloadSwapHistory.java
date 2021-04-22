package com.binancetracker.repo.api;

import com.binance.api.client.factory.BinanceSwapApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.DownloadLiquidSwapHistoryRunner;
import com.binancetracker.repo.api.runnable.DownloadSwapHistoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;

public class DownloadSwapHistory extends DownloadHistory{

    private DownloadSwapHistoryRunner swapHistoryRunner;
    private DownloadLiquidSwapHistoryRunner downloadLiquidSwapHistoryRunner;

    public DownloadSwapHistory(BinanceSwapApiClientFactory binanceSwapApiClientFactory,SingletonDataBase singletonDataBase)
    {
        swapHistoryRunner = new DownloadSwapHistoryRunner(binanceSwapApiClientFactory,singletonDataBase);
        downloadLiquidSwapHistoryRunner = new DownloadLiquidSwapHistoryRunner(binanceSwapApiClientFactory,singletonDataBase);
    }


    @Override
    public void setMessageEventListner(ClientFactoryRunner.MessageEvent messageEventListner) {
        swapHistoryRunner.setMessageEventListner(messageEventListner);
        downloadLiquidSwapHistoryRunner.setMessageEventListner(messageEventListner);
    }

    public void downloadSwapHistory()
    {
        RestExecuter.addTask(swapHistoryRunner);
    }

    public void downloadLiquidSwapHistory()
    {
        RestExecuter.addTask(downloadLiquidSwapHistoryRunner);
    }
}
