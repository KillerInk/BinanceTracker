package com.binancetracker.repo.api.runnable;


import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.HistoryTradeEntity;
import com.binancetracker.repo.room.entity.Market;
import com.binancetracker.ui.main.AssetModel;

import java.util.HashMap;
import java.util.List;

public class DownloadLastTradeHistoryRunner extends DownloadTradeFullHistoryRunner {
    public DownloadLastTradeHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void processRun() {
        List<AssetModel> assetModels = singletonDataBase.appDatabase.assetModelDao().getAll();
        HashMap<String,Market> marketsToCheck = new HashMap();
        for (AssetModel model : assetModels)
        {
            List<Market> markets = singletonDataBase.binanceDatabase.marketDao().findByAsset(model.assetName);
            for (Market m : markets)
                marketsToCheck.put(m.baseAsset+m.quoteAsset,m);
        }
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        for (Market market : marketsToCheck.values())
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
