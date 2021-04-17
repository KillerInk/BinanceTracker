package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Deposit;
import com.binancetracker.room.SingletonDataBase;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class Download30DaysDepositHistoryRunner extends DownloadDepositFullHistoryRunner {

    private final String TAG = Download30DaysDepositHistoryRunner.class.getSimpleName();
    public Download30DaysDepositHistoryRunner(BinanceApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void run() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days30;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        List<Deposit> depositHistory = client.getWalletEndPoint().getDepositHistory(starttime,endtime);
        if (depositHistory != null)
        {
            addItemToDB(depositHistory);
        }
    }
}
