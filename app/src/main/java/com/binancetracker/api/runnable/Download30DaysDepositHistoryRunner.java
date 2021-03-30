package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

import java.text.DateFormat;
import java.util.Date;

public class Download30DaysDepositHistoryRunner extends DownloadDepositFullHistoryRunner {

    private final String TAG = Download30DaysDepositHistoryRunner.class.getSimpleName();
    public Download30DaysDepositHistoryRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days90;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        com.binance.api.client.domain.account.DepositHistory depositHistory = client.getDepositHistory(starttime,endtime);
        if (depositHistory != null && depositHistory.getDepositList() != null)
        {
            addItemToDB(depositHistory.getDepositList());
        }
    }
}
