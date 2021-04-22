package com.binancetracker.repo.api.runnable;

import com.binance.api.client.api.sync.BinanceApiSwapRestClient;
import com.binance.api.client.domain.account.SwapHistory;
import com.binance.api.client.factory.BinanceSwapApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.SwapHistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class DownloadSwapHistoryRunner extends ClientFactoryRunner<BinanceSwapApiClientFactory> {
    public DownloadSwapHistoryRunner(BinanceSwapApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void run() {

        fireOnSyncStart(0);
        BinanceApiSwapRestClient client = clientFactory.newRestClient();
        List<SwapHistory> swapHistory = client.getSwapHistory();
        if (swapHistory != null)
        {
            List<SwapHistoryEntity> entities = new ArrayList<>();
            for (SwapHistory s : swapHistory)
            {
                SwapHistoryEntity entity = new SwapHistoryEntity();
                entity.baseAsset = s.getBaseAsset();
                entity.baseQty = s.getBaseQty();
                entity.fee = s.getFee();
                entity.price = s.getPrice();
                entity.quoteAsset = s.getQuoteAsset();
                entity.status = s.getStatus();
                entity.quoteQty = s.getQuoteQty();
                entity.swapId = s.getSwapId();
                entity.swapTime = s.getSwapTime();
                entities.add(entity);
            }
            singletonDataBase.binanceDatabase.swapHistoryDao().insertAll(entities);
        }
        fireOnSyncEnd();
    }
}
