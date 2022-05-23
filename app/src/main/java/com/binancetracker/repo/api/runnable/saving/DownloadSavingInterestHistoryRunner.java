package com.binancetracker.repo.api.runnable.saving;

import static com.binance.api.client.impl.BinanceApiServiceGenerator.getTime;

import com.binance.api.client.api.sync.BinanceApiSavingRestClient;
import com.binance.api.client.domain.saving.InterestHistory;
import com.binance.api.client.factory.BinanceSavingApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.JsonToDBConverter;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.InterestHistoryEntity;
import com.binancetracker.utils.MyTime;

import java.util.ArrayList;
import java.util.List;

public class DownloadSavingInterestHistoryRunner extends ClientFactoryRunner<BinanceSavingApiClientFactory> {
    public DownloadSavingInterestHistoryRunner(BinanceSavingApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void processRun() {
        BinanceApiSavingRestClient client = clientFactory.newRestClient();
        downloadPerPage(client);
       /* MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-365 * 3);
        BinanceApiSavingRestClient client = clientFactory.newRestClient();
        List<InterestHistory> dailys = client.getInterestHistory("DAILY",null,null,null,null,100L,5000L,System.currentTimeMillis());
        List<InterestHistory> activity = client.getInterestHistory("ACTIVITY",null,null,null,null,100L,5000L,System.currentTimeMillis());
        List<InterestHistory> CUSTOMIZED_FIXED = client.getInterestHistory("CUSTOMIZED_FIXED",null,null,null,null,100L,5000L,System.currentTimeMillis());
        addToDB(dailys);
        addToDB(activity);
        addToDB(CUSTOMIZED_FIXED);*/
    }

    private List<InterestHistory> getInterestHistory(BinanceApiSavingRestClient client,String lendingType, MyTime starttime, MyTime endtime)
    {
        return client.getInterestHistory(lendingType,null,starttime.getUtcTime(),endtime.getUtcTime(),null,100L,5000L);
    }

    private List<InterestHistory> getInterestHistory(BinanceApiSavingRestClient client,String lendingType,int page)
    {
        return client.getInterestHistory(lendingType,null,null,null , (long) page,100L,5000L);
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
        List<InterestHistory> dailys = getInterestHistory(client,lendingtype,i);
        fireOnSyncUpdate(i, "page for " +lendingtype);
        if (dailys != null && dailys.size() >0) {
            addToDB(dailys);
            if (dailys.size() == 100)
            {
                i++;
                perPage(client,lendingtype,i);
            }
        }
    }

    private void addToDB(List<InterestHistory> interestHistoryList)
    {
        List<InterestHistoryEntity> entities = new ArrayList<>();
        for (InterestHistory history : interestHistoryList)
            entities.add(JsonToDBConverter.getInterestHistoryEntity(history));
        singletonDataBase.binanceDatabase.interestHistoryDao().insertAll(entities);
    }
}
