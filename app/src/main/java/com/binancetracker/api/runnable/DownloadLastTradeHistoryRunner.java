package com.binancetracker.api.runnable;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.HistoryTrade;

import java.util.List;

public class DownloadLastTradeHistoryRunner extends DownloadTradeFullHistoryRunner {
    public DownloadLastTradeHistoryRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        List<String> tradedPairs = SingletonDataBase.appDatabase.historyTradeDao().getTradedPairs();
        if (tradedPairs != null && tradedPairs.size() >0)
        {
            BinanceApiRestClient client = clientFactory.newRestClient();
            for (String pair : tradedPairs)
            {
                HistoryTrade trade = SingletonDataBase.appDatabase.historyTradeDao().getLastTradeBySymbol(pair);
                if (trade != null) {
                    List<Trade> histtrades = client.getMyTrades(pair, trade.id);
                    insertHistory(client, SingletonDataBase.appDatabase.historyTradeDao(), pair, histtrades);
                }
            }
        }
    }
}
