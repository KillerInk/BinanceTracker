package com.binancetracker.repo.api.runnable.futures;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.FuturesTransactionHistory;
import com.binance.api.client.domain.account.FuturesTransactionList;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.MyApplication;
import com.binancetracker.R;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.JsonToDBConverter;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class DownloadFuturesTransactionHistory extends ClientFactoryRunner<BinanceSpotApiClientFactory> {

    public DownloadFuturesTransactionHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void processRun() {
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        List<String> assets = singletonDataBase.appDatabase.assetModelDao().getAllAssets();
        singletonDataBase.binanceDatabase.futuresTransactionHistoryDao().deleteAll();
        fireOnSyncStart(assets.size());
        int i = 0;
        for(String a : assets)
        {
            fireOnSyncUpdate(i++,a);
            if (!isFiat(a)) {
                FuturesTransactionList historyList = client.getWalletEndPoint().getFutureTransactionHistory(a, 1571436000000L);

                if (historyList != null && historyList.getRows() != null) {
                    List<FuturesTransactionHistoryEntity> entities = new ArrayList<>();
                    for (FuturesTransactionHistory transactionHistory : historyList.getRows()) {
                        FuturesTransactionHistoryEntity entity = JsonToDBConverter.getFuturesTransactionHistoryEntity(transactionHistory);
                        entities.add(entity);
                    }
                    singletonDataBase.binanceDatabase.futuresTransactionHistoryDao().insertAll(entities);
                }
            }
        }
        fireOnSyncEnd();
    }



    private boolean isFiat(String in)
    {
        String[] fiat = MyApplication.getStringArrayFromRes(R.array.fiats);
        for (String f : fiat) {
            if (in.equals(f))
                return true;
        }
        return  false;
    }
}
