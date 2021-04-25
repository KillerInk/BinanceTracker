package com.binancetracker.repo.api.runnable;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Deposit;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.DepositHistoryEntity;
import com.binancetracker.utils.MyTime;

import java.util.List;

public class DownloadDepositFullHistoryRunner extends ClientFactoryRunner<BinanceSpotApiClientFactory> {
    private final String TAG = DownloadDepositFullHistoryRunner.class.getSimpleName();
    protected final static long days30 = 2592000000L; // 30days
    protected final static int checkyears = 3*365/30; //3years split into 30days

    public DownloadDepositFullHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void processRun() {
        singletonDataBase.binanceDatabase.depositHistoryDao().deleteAll();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-30);
        Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        fireOnSyncStart(checkyears);
        for (int i = 0; i < checkyears; i++) {
            List<Deposit> deposits = client.getWalletEndPoint().getDepositHistory(starttime.getTime(),endtime.getTime());
            if (deposits != null)
            {
                addItemToDB(deposits);
            }
            fireOnSyncUpdate(i,"");
            endtime.setDays(-30).setDayToEnd();
            starttime.setDays(-30).setDayToBegin();
            Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        }
        fireOnSyncEnd();
    }

    protected void addItemToDB(List<Deposit> depositHistory) {
        if (depositHistory != null) {
            List<Deposit> deposits = depositHistory;
            if (deposits != null) {
                for (Deposit deposit : deposits) {
                    DepositHistoryEntity dhe = new DepositHistoryEntity();
                    dhe.id = Long.parseLong(deposit.getInsertTime());
                    dhe.asset = deposit.getAsset();
                    dhe.amount = Double.parseDouble(deposit.getAmount());
                    dhe.insertTime = Long.parseLong(deposit.getInsertTime());
                    dhe.txId = deposit.getTxId();
                    singletonDataBase.binanceDatabase.depositHistoryDao().insert(dhe);
                }
            }
        }
        Log.d(TAG,"download Deposit list done");
    }
}
