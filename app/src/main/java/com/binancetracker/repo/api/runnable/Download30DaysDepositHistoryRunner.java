package com.binancetracker.repo.api.runnable;

import android.util.Log;

import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Deposit;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.MyTime;

import java.util.List;

public class Download30DaysDepositHistoryRunner extends DownloadDepositFullHistoryRunner {

    private final String TAG = Download30DaysDepositHistoryRunner.class.getSimpleName();
    public Download30DaysDepositHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory,singletonDataBase);
    }

    @Override
    public void run() {
        BinanceApiSpotRestClient client = clientFactory.newRestClient();
        MyTime endtime = new MyTime(System.currentTimeMillis());
        MyTime starttime = new MyTime(endtime.getTime()).setDays(-30);
        Log.d(TAG, "startTime:" + starttime.getString()+ " endTime:" + endtime.getString());
        List<Deposit> depositHistory = client.getWalletEndPoint().getDepositHistory(starttime.getTime(),endtime.getTime());
        if (depositHistory != null)
        {
            addItemToDB(depositHistory);
        }
    }
}
