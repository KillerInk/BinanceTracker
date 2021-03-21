package com.binancetracker.utils;

import android.util.Log;

import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Profit;

import java.util.HashMap;
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
                HashMap<String, Profit> assets = new HashMap<>();

                for (String pair : tradedPairs)
                {
                    MarketPair mpair = new MarketPair(pair);
                    List<HistoryTrade> trades = SingletonDataBase.appDatabase.historyTradeDao().findByName(pair);
                    Profit quoteass = getQuoteAsset(mpair,assets);
                    Profit baseass = getBaseAsset(mpair,assets);
                    for (HistoryTrade ht : trades)
                    {
                        if (ht.buyer)
                        {
                            quoteass.profit += Double.parseDouble(ht.qty);
                            baseass.profit -= Double.parseDouble(ht.quoteQty);
                        }
                        else
                        {
                            quoteass.profit -= Double.parseDouble(ht.qty);
                            baseass.profit += Double.parseDouble(ht.quoteQty);
                        }
                    }
                    Log.d(this.getClass().getSimpleName(),"Pair:" + pair + " count:" + trades.size() + " " +quoteass.asset + ":" + quoteass.profit + " " + baseass.asset +":" +baseass.profit);
                }
                for (Profit profit : assets.values())
                    SingletonDataBase.appDatabase.profitDao().insert(profit);
                Log.d(this.getClass().getSimpleName(),"Profit calc done");
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
