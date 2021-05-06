package com.binancetracker.repo.api.runnable;


import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.HistoryTradeEntity;
import com.binancetracker.repo.room.entity.Market;

import java.util.List;

public class DownloadLastTradeHistoryRunner extends DownloadTradeFullHistoryRunner {
    public DownloadLastTradeHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void processRun() {
        List<Market> markets = singletonDataBase.binanceDatabase.marketDao().getAll();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        for (Market market : markets)
        {
            String pair = market.baseAsset+market.quoteAsset;
            HistoryTradeEntity trade = singletonDataBase.binanceDatabase.historyTradeDao().getLastTradeBySymbol(pair);
            if (trade != null) {
                List<Trade> histtrades = client.getMyTrades(pair, trade.id);
                insertHistory(client, singletonDataBase.binanceDatabase.historyTradeDao(), pair, histtrades);
            }
        }
    }
}
