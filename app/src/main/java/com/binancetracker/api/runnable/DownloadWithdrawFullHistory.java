package com.binancetracker.api.runnable;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Withdraw;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.WithdrawHistoryEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class DownloadWithdrawFullHistory extends DownloadDepositFullHistoryRunner {

    private final String TAG = DownloadWithdrawFullHistory.class.getSimpleName();

    public DownloadWithdrawFullHistory(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        SingletonDataBase.binanceDatabase.withdrawHistoryDao().deleteAll();
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days30;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        for (int i = 0; i < checkyears; i++) {
            List<Withdraw> withdraws = client.getWalletEndPoint().getWithdrawHistory(starttime,endtime);
            if (withdraws != null)
            {
                addWithdrawItemToDB(withdraws);
            }
            endtime = endtime - days30;
            starttime = starttime - days30;
        }
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
                    SingletonDataBase.binanceDatabase.withdrawHistoryDao().insert(dhe);
                }
            }
        }
    }
}
