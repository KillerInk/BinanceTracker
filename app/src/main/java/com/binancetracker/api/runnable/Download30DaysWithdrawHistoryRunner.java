package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

import java.text.DateFormat;
import java.util.Date;

public class Download30DaysWithdrawHistoryRunner extends DownloadWithdrawFullHistory {

    private final String TAG = Download30DaysWithdrawHistoryRunner.class.getSimpleName();

    public Download30DaysWithdrawHistoryRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days30;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        com.binance.api.client.domain.account.WithdrawHistory depositHistory = client.getWithdrawHistory(starttime,endtime);
        if (depositHistory != null && depositHistory.getWithdrawList() != null)
        {
            addWithdrawItemToDB(depositHistory.getWithdrawList());
        }
    }
}
