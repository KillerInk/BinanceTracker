package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Deposit;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.DepositHistoryEntity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DownloadDepositFullHistoryRunner extends ClientFactoryRunner {
    private final String TAG = DownloadDepositFullHistoryRunner.class.getSimpleName();
    protected final static long days90 = 2592000000L; // 30days
    protected final static int checkyears = 3*365/30; //3years split into 30days
    public DownloadDepositFullHistoryRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days90;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        for (int i = 0; i < checkyears; i++) {
            com.binance.api.client.domain.account.DepositHistory depositHistory = client.getDepositHistory(starttime,endtime);
            if (depositHistory != null && depositHistory.getDepositList() != null)
            {
                addItemToDB(depositHistory.getDepositList());
            }
            endtime = endtime - days90;
            starttime = starttime - days90;
        }
    }

    protected void addItemToDB(List<Deposit> depositHistory) {
        if (depositHistory != null) {
            List<Deposit> deposits = depositHistory;
            if (deposits != null) {
                for (Deposit deposit : deposits) {
                    DepositHistoryEntity dhe = new DepositHistoryEntity();
                    dhe.asset = deposit.getAsset();
                    dhe.amount = Double.parseDouble(deposit.getAmount());
                    dhe.insertTime = Long.parseLong(deposit.getInsertTime());
                    dhe.txId = deposit.getTxId();
                    SingletonDataBase.appDatabase.depositHistoryDao().insert(dhe);
                }
            }
        }
    }
}
