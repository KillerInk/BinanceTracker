package com.binancetracker.utils;

import android.util.Log;

import com.binancetracker.api.TradeHistory;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.HistoryTrade;

import java.util.List;

public class CalcProfits
{

    public void calcProfits()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> tradedPairs = SingletonDataBase.appDatabase.historyTradeDao().getTradedPairs();
                Log.d(this.getClass().getSimpleName(),"Pairs:" + tradedPairs.toString());
                for (String pair : tradedPairs)
                {
                    List<HistoryTrade> trades = SingletonDataBase.appDatabase.historyTradeDao().findByName(pair);
                    Log.d(this.getClass().getSimpleName(),"Pair:" + pair + " count:" + trades.size());
                }
            }
        }).start();

    }
}
