package com.binancetracker.utils;

import android.util.Log;

import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.DespositHistoryEntity;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Profit;

import java.util.HashMap;
import java.util.List;

public class CalcProfits
{
    private final String TAG = CalcProfits.class.getSimpleName();
    public void calcProfits()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> tradedPairs = SingletonDataBase.appDatabase.historyTradeDao().getTradedPairs();
                Log.d(TAG,"Pairs:" + tradedPairs.toString());
                HashMap<String, Profit> assets = new HashMap<>();

                for (String pair : tradedPairs)
                {
                    Log.d(TAG,"Pair:" + pair);
                    MarketPair mpair = new MarketPair(pair);
                    List<HistoryTrade> trades = SingletonDataBase.appDatabase.historyTradeDao().findByName(pair);
                    Profit quoteass = getQuoteAsset(mpair,assets);
                    Profit baseass = getBaseAsset(mpair,assets);

                    for (HistoryTrade ht : trades)
                    {
                        if (ht.buyer)
                        {
                            quoteass.profit += Double.parseDouble(ht.qty);
                            quoteass.tradescount++;
                            baseass.profit -= Double.parseDouble(ht.quoteQty);
                        }
                        else
                        {
                            quoteass.profit -= Double.parseDouble(ht.qty);
                            baseass.profit += Double.parseDouble(ht.quoteQty);
                            baseass.tradescount++;
                        }
                    }
                    Log.d(TAG,"Pair:" + pair + " count:" + trades.size() + " " +quoteass.asset + ":" + quoteass.profit + " " + baseass.asset +":" +baseass.profit);
                }

                for (String s : assets.keySet()) {

                    List<DespositHistoryEntity> deposits = SingletonDataBase.appDatabase.despositHistoryDao().findByName(s);
                    Profit profit = assets.get(s);
                    if (deposits != null && deposits.size() > 0) {
                        for (DespositHistoryEntity d : deposits) {
                            profit.profit += d.amount;
                            profit.deposits += d.amount;
                        }
                    }
                }
                for (Profit profit : assets.values())
                    SingletonDataBase.appDatabase.profitDao().insert(profit);
                Log.d(TAG,"Profit calc done");
            }
        }).start();
    }

    private Profit getQuoteAsset(MarketPair mpair,HashMap<String, Profit> assets)
    {
        Profit quoteass = assets.get(mpair.getQuoteAsset());
        if (quoteass == null) {
            quoteass = new Profit();
            quoteass.asset = mpair.getQuoteAsset();
            assets.put(quoteass.asset,quoteass);
        }
        return quoteass;
    }

    private Profit getBaseAsset(MarketPair mpair,HashMap<String, Profit> assets)
    {
        Profit quoteass = assets.get(mpair.getBaseAsset());
        if (quoteass == null) {
            quoteass = new Profit();
            quoteass.asset = mpair.getBaseAsset();
            assets.put(quoteass.asset,quoteass);
        }
        return quoteass;
    }
}
