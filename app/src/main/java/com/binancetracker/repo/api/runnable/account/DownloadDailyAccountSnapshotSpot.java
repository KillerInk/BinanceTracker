package com.binancetracker.repo.api.runnable.account;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.account.MarginAssetBalance;
import com.binance.api.client.domain.account.snapshot.DailyAccountSnapShotSpot;
import com.binance.api.client.domain.account.snapshot.DailyAccountSnapshot;
import com.binance.api.client.domain.account.snapshot.DailyAccountSnapshotFutures;
import com.binance.api.client.domain.account.snapshot.DailyAccountSnapshotMargin;
import com.binance.api.client.domain.account.snapshot.FuturesSnapshotAsset;
import com.binance.api.client.domain.account.snapshot.SnapshotVos;
import com.binance.api.client.domain.account.snapshot.data.FuturesData;
import com.binance.api.client.domain.account.snapshot.data.MarginData;
import com.binance.api.client.domain.account.snapshot.data.SpotData;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.JsonToDBConverter;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.DailyAccountSnapshotEntity;
import com.binancetracker.utils.MyTime;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

public class DownloadDailyAccountSnapshotSpot extends ClientFactoryRunner<BinanceSpotApiClientFactory> {

    private static final String TAG = DownloadDailyAccountSnapshotSpot.class.getSimpleName();

    public DownloadDailyAccountSnapshotSpot(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }

    @Override
    public void processRun() {

        fireOnSyncStart(0);
        MyTime endTime = new MyTime().setDayToEnd();
        MyTime startTime = new MyTime().setDays(-30).setDayToBegin();
        HashMap<Long, DailyAccountSnapshotEntity> entityHashMap = new HashMap();
        fireOnSyncUpdate(0,"process range: " + startTime.getString() + "-"+endTime.getString());
        Log.v(TAG,"process range: " + startTime.getString() + "-"+endTime.getString());
        boolean keepRequesting = true;
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        DailyAccountSnapShotSpot das = client.getDailyAccountSnapShotSpot(startTime.getTime(), endTime.getTime(), 30L);
        sleep(12000);
        client = clientFactory.newRestClient();
        DailyAccountSnapshotMargin dam = client.getDailyAccountSnapShotMargin(startTime.getTime(), endTime.getTime(), 30L);
        sleep(12000);
        client = clientFactory.newRestClient();
        DailyAccountSnapshotFutures daf = client.getDailyAccountSnapShotFutures(startTime.getTime(), endTime.getTime(), 30L);
        sleep(12000);

        while (keepRequesting) {
            if (das != null && das.snapshotVos != null && das.snapshotVos.size() > 0)
                processSpot(das,entityHashMap);
            if (dam != null && dam.snapshotVos != null && dam.snapshotVos.size() > 0)
                processMargin(dam, entityHashMap);
            if (daf != null && daf.snapshotVos != null && daf.snapshotVos.size() > 0)
                processFutures(daf, entityHashMap);
            singletonDataBase.binanceDatabase.dailyAccountSnapshotDao().insertAll(entityHashMap.values());
            entityHashMap.clear();


            startTime.setDays(-31).setDayToBegin();
            endTime.setDays(-31).setDayToEnd();
            fireOnSyncUpdate(0,"process range: " + startTime.getString() + "-"+endTime.getString());
            Log.v(TAG,"process range: " + startTime.getString() + "-"+endTime.getString());
            keepRequesting = false;
            if (das != null && das.snapshotVos != null && das.snapshotVos.size() > 0) {
                keepRequesting = true;
                client = clientFactory.newRestClient();
                das = client.getDailyAccountSnapShotSpot(startTime.getTime(), endTime.getTime(), 30L);
                sleep(12000);
            }
            if (dam != null && dam.snapshotVos != null && dam.snapshotVos.size() > 0) {
                keepRequesting = true;
                client = clientFactory.newRestClient();
                dam = client.getDailyAccountSnapShotMargin(startTime.getTime(), endTime.getTime(), 30L);
                sleep(12000);
            }
            if (daf != null && daf.snapshotVos != null && daf.snapshotVos.size() > 0) {
                keepRequesting = true;
                client = clientFactory.newRestClient();
                daf = client.getDailyAccountSnapShotFutures(startTime.getTime(), endTime.getTime(), 30L);
                sleep(12000);
            }
        }

        fireOnSyncEnd();
        Log.v(TAG, "done");
    }

    private void sleep(int sleep)
    {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processSpot(DailyAccountSnapShotSpot spot, HashMap<Long, DailyAccountSnapshotEntity> entityHashMap)
    {
        for(SnapshotVos<SpotData> vos : spot.snapshotVos)
        {
            for (AssetBalance balance : vos.data.balances)
            {
                if (!isZeroBalance(balance.getFree()) || !isZeroBalance(balance.getLocked())) {
                    DailyAccountSnapshotEntity entity = JsonToDBConverter.getDailyAccountSnapshotEntity(balance, vos.updateTime);
                    entityHashMap.put(entity.id, entity);
                }
            }
        }
    }

    private boolean isZeroBalance(String s)
    {
        return s.equals("0") || s.equals("0.0");
    }

    private void processMargin(DailyAccountSnapshotMargin margin, HashMap<Long, DailyAccountSnapshotEntity> entityHashMap)
    {
        for(SnapshotVos<MarginData> vos : margin.snapshotVos)
        {
            for (MarginAssetBalance balance :vos.data.userAssets)
            {
                if (!isZeroBalance(balance.getFree()) || !isZeroBalance(balance.getLocked())) {
                    DailyAccountSnapshotEntity entity = entityHashMap.get(vos.updateTime + balance.getAsset().hashCode());
                    if (entity == null)
                        entity = JsonToDBConverter.getNewEntity(balance.getAsset(), vos.updateTime);
                    JsonToDBConverter.applyMarginData(entity, balance, vos.updateTime);
                    entityHashMap.put(entity.id, entity);
                }
            }
        }
    }

    private void processFutures(DailyAccountSnapshotFutures futures, HashMap<Long, DailyAccountSnapshotEntity> entityHashMap)
    {
        for(SnapshotVos<FuturesData> vos : futures.snapshotVos)
        {
            for (FuturesSnapshotAsset balance :vos.data.assets)
            {
                if (!isZeroBalance(balance.marginBalance) || !isZeroBalance(balance.walletBalance)) {
                    DailyAccountSnapshotEntity entity = entityHashMap.get(vos.updateTime + balance.asset.hashCode());
                    if (entity == null)
                        entity = JsonToDBConverter.getNewEntity(balance.asset, vos.updateTime);
                    JsonToDBConverter.applyFuturesData(entity, balance, vos.updateTime);
                    entityHashMap.put(entity.id, entity);
                }
            }
        }
    }
}
