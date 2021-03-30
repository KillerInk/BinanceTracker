package com.binancetracker.api;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Deposit;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.DespositHistoryEntity;
import com.binancetracker.thread.RestExecuter;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DownloadDepositHistory
{
    private final  String TAG = DownloadDepositHistory.class.getSimpleName();
    private BinanceApiClientFactory clientFactory;
    private final long days90;
    private final int checkyears;

    public DownloadDepositHistory(BinanceApiClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
        days90 = 2592000000L; //7603200000L;//88days //7689600000L; //89days
        checkyears = 3*365/30;
    }

    public void downloadFullHistory()
    {
        RestExecuter.addTask(new Runnable() {
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
        });
    }

    private void addItemToDB(List<Deposit> depositHistory) {
        if (depositHistory != null) {
            List<Deposit> deposits = depositHistory;
            if (deposits != null) {
                for (Deposit deposit : deposits) {
                    DespositHistoryEntity dhe = new DespositHistoryEntity();
                    dhe.asset = deposit.getAsset();
                    dhe.amount = Double.parseDouble(deposit.getAmount());
                    dhe.insertTime = Long.parseLong(deposit.getInsertTime());
                    dhe.txId = deposit.getTxId();
                    SingletonDataBase.appDatabase.despositHistoryDao().insert(dhe);
                }
            }
        }
    }


}
