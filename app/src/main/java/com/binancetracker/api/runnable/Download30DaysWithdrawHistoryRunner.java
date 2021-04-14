package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Withdraw;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

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
        List<Withdraw> withdraws = client.getWalletEndPoint().getWithdrawHistory(starttime,endtime);
        if (withdraws != null)
        {
            addWithdrawItemToDB(withdraws);
        }
    }
}
