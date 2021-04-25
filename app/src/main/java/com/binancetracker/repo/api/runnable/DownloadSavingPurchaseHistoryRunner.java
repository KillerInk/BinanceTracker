package com.binancetracker.repo.api.runnable;

import com.binance.api.client.api.sync.BinanceApiSavingRestClient;
import com.binance.api.client.domain.saving.PurchaseRecord;
import com.binance.api.client.factory.BinanceSavingApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.PurchaseRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class DownloadSavingPurchaseHistoryRunner extends ClientFactoryRunner<BinanceSavingApiClientFactory> {


    public DownloadSavingPurchaseHistoryRunner(BinanceSavingApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void processRun() {
        fireOnSyncStart(3);
        BinanceApiSavingRestClient client = clientFactory.newRestClient();
        List<PurchaseRecord> daily = client.getPurchaseRecord("DAILY",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addListToDB(daily);
        fireOnSyncUpdate(1,"DAILY");
        List<PurchaseRecord> ACTIVITY = client.getPurchaseRecord("ACTIVITY",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addListToDB(ACTIVITY);
        fireOnSyncUpdate(1,"CUSTOMIZED_FIXED");
        List<PurchaseRecord> CUSTOMIZED_FIXED = client.getPurchaseRecord("CUSTOMIZED_FIXED",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addListToDB(CUSTOMIZED_FIXED);
        fireOnSyncEnd();
    }

    private PurchaseRecordEntity getEntity(PurchaseRecord record)
    {
        PurchaseRecordEntity purchaseRecordEntity = new PurchaseRecordEntity();
        purchaseRecordEntity.amount = record.getAmount();
        purchaseRecordEntity.asset = record.getAsset();
        purchaseRecordEntity.creatTime = record.getCreatTime();
        purchaseRecordEntity.lot = record.getLot();
        purchaseRecordEntity.productName = record.getProductName();
        purchaseRecordEntity.purchaseId = record.getPurchaseId();
        purchaseRecordEntity.status =record.getStatus();
        return purchaseRecordEntity;
    }

    private void addListToDB(List<PurchaseRecord> records)
    {
        List<PurchaseRecordEntity> entities = new ArrayList<>();
        for (PurchaseRecord record : records)
            entities.add(getEntity(record));
        singletonDataBase.binanceDatabase.purchaseRecordDao().insertAll(entities);
    }
}
