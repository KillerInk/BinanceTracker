package com.binancetracker.api.runnable;

import android.util.Log;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Deposit;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.DepositHistoryEntity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DownloadDepositFullHistoryRunner extends ClientFactoryRunner {
    private final String TAG = DownloadDepositFullHistoryRunner.class.getSimpleName();
    protected final static long days30 = 2592000000L; // 30days
    protected final static int checkyears = 3*365/30; //3years split into 30days
    public DownloadDepositFullHistoryRunner(BinanceApiClientFactory clientFactory) {
        super(clientFactory);
    }

    @Override
    public void run() {
        SingletonDataBase.binanceDatabase.depositHistoryDao().deleteAll();
        BinanceApiRestClient client = clientFactory.newRestClient();
        long endtime = System.currentTimeMillis();
        long starttime = endtime - days30;
        Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        for (int i = 0; i < checkyears; i++) {
            List<Deposit> deposits = client.getWalletEndPoint().getDepositHistory(starttime,endtime);
            if (deposits != null)
            {
                addItemToDB(deposits);
            }
            endtime = endtime - days30;
            starttime = starttime - days30;
            Log.d(TAG, "startTime:" + DateFormat.getDateTimeInstance().format(new Date(starttime)) + " endTime:" + DateFormat.getDateTimeInstance().format(new Date(endtime)));
        }
    }

    protected void addItemToDB(List<Deposit> depositHistory) {
        if (depositHistory != null) {
            List<Deposit> deposits = depositHistory;
            if (deposits != null) {
                for (Deposit deposit : deposits) {
                    DepositHistoryEntity dhe = new DepositHistoryEntity();
                    dhe.id = Long.parseLong(deposit.getInsertTime());
                    dhe.asset = deposit.getAsset();
                    dhe.amount = Double.parseDouble(deposit.getAmount());
                    dhe.insertTime = Long.parseLong(deposit.getInsertTime());
                    dhe.txId = deposit.getTxId();
                    SingletonDataBase.binanceDatabase.depositHistoryDao().insert(dhe);
                }
            }
        }
        Log.d(TAG,"download Deposit list done");
    }
}
