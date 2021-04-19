package com.binancetracker.repo.api.runnable;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Withdraw;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.MyTime;
import java.util.List;

public class Download30DaysWithdrawHistoryRunner extends DownloadWithdrawFullHistory {

    private final String TAG = Download30DaysWithdrawHistoryRunner.class.getSimpleName();

    public Download30DaysWithdrawHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void run() {
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-30);
        Log.d(TAG, "startTime:" + starttime.getString() + " endTime:" + endtime.getString());
        List<Withdraw> withdraws = client.getWalletEndPoint().getWithdrawHistory(starttime.getTime(),endtime.getTime());
        if (withdraws != null)
        {
            addWithdrawItemToDB(withdraws);
        }
    }
}
