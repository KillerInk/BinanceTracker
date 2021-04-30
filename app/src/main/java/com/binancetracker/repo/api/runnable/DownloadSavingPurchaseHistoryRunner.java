package com.binancetracker.repo.api.runnable;

import com.binance.api.client.api.sync.BinanceApiSavingRestClient;
import com.binance.api.client.domain.saving.InterestHistory;
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
        BinanceApiSavingRestClient client = clientFactory.newRestClient();
        downloadPerPage(client);
        /*fireOnSyncStart(3);
        BinanceApiSavingRestClient client = clientFactory.newRestClient();
        List<PurchaseRecord> daily = client.getPurchaseRecord("DAILY",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addListToDB(daily);
        fireOnSyncUpdate(1,"DAILY");
        List<PurchaseRecord> ACTIVITY = client.getPurchaseRecord("ACTIVITY",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addListToDB(ACTIVITY);
        fireOnSyncUpdate(1,"CUSTOMIZED_FIXED");
        List<PurchaseRecord> CUSTOMIZED_FIXED = client.getPurchaseRecord("CUSTOMIZED_FIXED",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addListToDB(CUSTOMIZED_FIXED);
        fireOnSyncEnd();*/
    }

    private List<PurchaseRecord> getPurchaseHistory(BinanceApiSavingRestClient client, String lendingType, int page)
    {
        return client.getPurchaseRecord(lendingType,null,null,null , (long) page,100L,5000L,System.currentTimeMillis());
    }

    private void downloadPerPage(BinanceApiSavingRestClient client)
    {
        fireOnSyncStart(0);
        int i = 1;
        perPage(client,"DAILY",i);
        i = 1;
        perPage(client,"ACTIVITY",i);
        i = 1;
        perPage(client,"CUSTOMIZED_FIXED",i);
        fireOnSyncEnd();
    }

    private void perPage(BinanceApiSavingRestClient client,String lendingtype, int i)
    {
        List<PurchaseRecord> dailys = getPurchaseHistory(client,lendingtype,i);
        fireOnSyncUpdate(i, "page for " + lendingtype);
        if (dailys != null && dailys.size() >0) {
            addListToDB(dailys);
            if (dailys.size() == 100)
            {
                i++;
                perPage(client,lendingtype,i);
            }
        }
    }



    private void addListToDB(List<PurchaseRecord> records)
    {
        List<PurchaseRecordEntity> entities = new ArrayList<>();
        for (PurchaseRecord record : records)
            entities.add(JsonToDBConverter.getPurchaseEntity(record));
        singletonDataBase.binanceDatabase.purchaseRecordDao().insertAll(entities);
    }
}
