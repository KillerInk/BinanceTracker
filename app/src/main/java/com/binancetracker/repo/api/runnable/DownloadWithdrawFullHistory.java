package com.binancetracker.repo.api.runnable;

import android.util.Log;
;
import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Withdraw;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;
import com.binancetracker.utils.MyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DownloadWithdrawFullHistory extends DownloadDepositFullHistoryRunner {

    private final String TAG = DownloadWithdrawFullHistory.class.getSimpleName();

    public DownloadWithdrawFullHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void run() {
        singletonDataBase.binanceDatabase.withdrawHistoryDao().deleteAll();
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-30);
        Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        fireOnSyncStart(checkyears);
        for (int i = 0; i < checkyears; i++) {
            List<Withdraw> withdraws = client.getWalletEndPoint().getWithdrawHistory(starttime.getTime(),endtime.getTime());
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
                    WithdrawHistoryEntity dhe = new WithdrawHistoryEntity();
                    dhe.id = (long)deposit.getApplyTime().hashCode();
                    dhe.asset = deposit.getAsset();
                    dhe.amount = Double.parseDouble(deposit.getAmount());
                    //'2020-12-23 19:38:57'
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date d = f.parse(deposit.getApplyTime());
                        dhe.applyTime = d.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (deposit.getSuccessTime() != null)
                        dhe.successTime = Long.parseLong(deposit.getSuccessTime());
                    if (deposit.getAddress() != null)
                        dhe.address = deposit.getAddress();
                    if (deposit.getTxId() != null)
                        dhe.txId = deposit.getTxId();
                    singletonDataBase.binanceDatabase.withdrawHistoryDao().insert(dhe);
                }
            }
        }
    }
}
