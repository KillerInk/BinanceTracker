package com.binancetracker.repo.api.runnable;


import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.HistoryTradeEntity;

import java.util.List;

public class DownloadLastTradeHistoryRunner extends DownloadTradeFullHistoryRunner {
    public DownloadLastTradeHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void processRun() {
        List<String> tradedPairs = singletonDataBase.binanceDatabase.historyTradeDao().getTradedPairs();
        if (tradedPairs != null && tradedPairs.size() >0)
        {
            BinanceApiSpotRestClient client = clientFactory.newRestClient();
            for (String pair : tradedPairs)
            {
                HistoryTradeEntity trade = singletonDataBase.binanceDatabase.historyTradeDao().getLastTradeBySymbol(pair);
                if (trade != null) {
                    List<Trade> histtrades = client.getMyTrades(pair, trade.id);
                    insertHistory(client, singletonDataBase.binanceDatabase.historyTradeDao(), pair, histtrades);
                }
            }
        }
    }
}
