package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Withdraw;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.WithdrawHistoryEntity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DownloadWithdrawFullHistory extends DownloadDepositFullHistoryRunner {

    private final String TAG = DownloadWithdrawFullHistory.class.getSimpleName();

    public DownloadWithdrawFullHistory(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days90;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        for (int i = 0; i < checkyears; i++) {
            com.binance.api.client.domain.account.WithdrawHistory depositHistory = client.getWithdrawHistory(starttime,endtime);
            if (depositHistory != null && depositHistory.getWithdrawList() != null)
            {
                addWithdrawItemToDB(depositHistory.getWithdrawList());
            }
            endtime = endtime - days90;
            starttime = starttime - days90;
        }
        Log.d(TAG, "DownloadWithdrawFullHistory done");
    }

    protected void addWithdrawItemToDB(List<Withdraw> depositHistory) {
        if (depositHistory != null) {
            List<Withdraw> deposits = depositHistory;
            if (deposits != null) {
                for (Withdraw deposit : deposits) {
                    WithdrawHistoryEntity dhe = new WithdrawHistoryEntity();
                    dhe.id = Long.parseLong(deposit.getApplyTime());
                    dhe.asset = deposit.getAsset();
                    dhe.amount = Double.parseDouble(deposit.getAmount());
                    dhe.applyTime = Long.parseLong(deposit.getApplyTime());
                    if (deposit.getSuccessTime() != null)
                        dhe.successTime = Long.parseLong(deposit.getSuccessTime());
                    if (deposit.getAddress() != null)
                        dhe.address = deposit.getAddress();
                    if (deposit.getTxId() != null)
                        dhe.txId = deposit.getTxId();
                    SingletonDataBase.appDatabase.withdrawHistoryDao().insert(dhe);
                }
            }
        }
    }
}
