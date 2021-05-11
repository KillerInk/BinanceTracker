package com.binancetracker.repo.api.runnable;

import com.binance.api.client.api.sync.BinanceApiSavingRestClient;
import com.binance.api.client.domain.saving.RedemptionRecord;
import com.binance.api.client.factory.BinanceSavingApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.RedemptionRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class DownloadSavingRedemptionHistoryRunner extends ClientFactoryRunner<BinanceSavingApiClientFactory> {

    public DownloadSavingRedemptionHistoryRunner(BinanceSavingApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void processRun() {
        BinanceApiSavingRestClient client = clientFactory.newRestClient();
        downloadPerPage(client);
    }

    private List<RedemptionRecord> getRedemptionHistory(BinanceApiSavingRestClient client, String lendingType, int page)
    {
        return client.getRedemptionRecord(lendingType,null,null,null , (long) page,100L,5000L,System.currentTimeMillis());
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
        List<RedemptionRecord> dailys = getRedemptionHistory(client,lendingtype,i);
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

    private void addListToDB(List<RedemptionRecord> records)
    {
        List<RedemptionRecordEntity> entities = new ArrayList<>();
        for (RedemptionRecord record : records)
            entities.add(JsonToDBConverter.getRedemptionEntity(record));
        singletonDataBase.binanceDatabase.redemptionRecordDao().insertAll(entities);
    }
}
