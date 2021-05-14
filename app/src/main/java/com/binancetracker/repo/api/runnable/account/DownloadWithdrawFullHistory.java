package com.binancetracker.repo.api.runnable.account;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Withdraw;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.api.runnable.JsonToDBConverter;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;
import com.binancetracker.utils.MyTime;

import java.util.List;

;

public class DownloadWithdrawFullHistory extends DownloadDepositFullHistoryRunner {

    private final String TAG = DownloadWithdrawFullHistory.class.getSimpleName();

    public DownloadWithdrawFullHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void processRun() {
        singletonDataBase.binanceDatabase.withdrawHistoryDao().deleteAll();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-30);
        Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        fireOnSyncStart(checkyears);
        for (int i = 0; i < checkyears; i++) {
            List<Withdraw> withdraws = client.getWalletEndPoint().getWithdrawHistory(starttime.getUtcTime(),endtime.getUtcTime());
            if (withdraws != null)
            {
                addWithdrawItemToDB(withdraws);
            }
            fireOnSyncUpdate(i,"");
            endtime.setDays(-30).setDayToEnd();
            starttime.setDays(-30).setDayToBegin();
        }
        fireOnSyncEnd();
        Log.d(TAG, "DownloadWithdrawFullHistory done");
    }

    protected void addWithdrawItemToDB(List<Withdraw> depositHistory) {
        if (depositHistory != null) {
            List<Withdraw> deposits = depositHistory;
            if (deposits != null) {
                for (Withdraw deposit : deposits) {
                    WithdrawHistoryEntity dhe = JsonToDBConverter.getWithdrawHistoryEntity(deposit);
                    singletonDataBase.binanceDatabase.withdrawHistoryDao().insert(dhe);
                }
            }
        }
    }
}
