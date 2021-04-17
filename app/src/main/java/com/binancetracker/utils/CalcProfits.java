package com.binancetracker.utils;

import android.util.Log;

import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.room.entity.DepositHistoryEntity;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.PortofolioHistory;
import com.binancetracker.room.entity.Profit;
import com.binancetracker.room.entity.WithdrawHistoryEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalcProfits
{
    private final String TAG = CalcProfits.class.getSimpleName();
    public void calcProfits(SingletonDataBase singletonDataBase)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                singletonDataBase.appDatabase.profitDao().deleteAll();
                List<String> tradedPairs = singletonDataBase.binanceDatabase.historyTradeDao().getTradedPairs();
                Log.d(TAG,"Pairs:" + tradedPairs.toString());
                HashMap<String, Profit> assets = new HashMap<>();

                for (String pair : tradedPairs)
                {
                    Log.d(TAG,"Pair:" + pair);
                    MarketPair mpair = new MarketPair(pair);
                    List<HistoryTrade> trades = singletonDataBase.binanceDatabase.historyTradeDao().findByName(pair);
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

                    Profit profit = assets.get(s);

                    List<DepositHistoryEntity> deposits = singletonDataBase.binanceDatabase.depositHistoryDao().findByName(s);
                    if (deposits != null && deposits.size() > 0) {
                        for (DepositHistoryEntity d : deposits) {
                            profit.profit += d.amount;
                            profit.deposits += d.amount;
                        }
                    }

                    List<WithdrawHistoryEntity> withdraws = singletonDataBase.binanceDatabase.withdrawHistoryDao().findByName(s);
                    if (withdraws != null && withdraws.size() > 0) {
                        for (WithdrawHistoryEntity d : withdraws) {
                            profit.profit -= d.amount;
                            profit.withdraws += d.amount;
                        }
                    }
                }
                for (Profit profit : assets.values())
                    singletonDataBase.appDatabase.profitDao().insert(profit);
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

    public void calcAssetLifeTimeHistory(SingletonDataBase singletonDataBase)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                calcAssetLifeTime(singletonDataBase);
            }
        }).start();
    }

    private void calcAssetLifeTime(SingletonDataBase singletonDataBase)
    {
        Log.d(TAG,"start calcAssetLifeTime");
        singletonDataBase.appDatabase.portofolioHistoryDao().deleteAll();
        long firstDepositTime = singletonDataBase.binanceDatabase.depositHistoryDao().getFirstDepositTime();
        Date startday = new Date(firstDepositTime);
        startday.setHours(0);
        startday.setMinutes(0);
        startday.setSeconds(0);
        Date endday = new Date(firstDepositTime);
        endday.setHours(23);
        endday.setMinutes(59);
        endday.setSeconds(59);
        Date finalDay = new Date(System.currentTimeMillis());
        finalDay.setHours(0);
        finalDay.setMinutes(0);
        finalDay.setSeconds(0);
        HashMap<Long, HashMap<String, PortofolioHistory>> historyHashmapTime = new HashMap<>();

        while (startday.getTime() <= finalDay.getTime()) {

            Date yesterday = new Date(startday.getTime());
            yesterday.setDate(yesterday.getDate() -1);
            HashMap<String, PortofolioHistory> yesterdayHistory = historyHashmapTime.get(yesterday.getTime());
            HashMap<String, PortofolioHistory> historyHashMap = new HashMap<>();

            fillAmountFromYesterday(yesterdayHistory,historyHashMap);
            addDepositsForDay(startday,endday,historyHashMap,singletonDataBase);
            addTradesForDay(startday,endday,historyHashMap,singletonDataBase);
            setDay_ID_Price(startday, endday, historyHashMap,singletonDataBase);

            historyHashmapTime.put(startday.getTime(), historyHashMap);

            Log.d(TAG,"Finished Day: " + startday.toString());
            startday.setHours(24);
            endday.setTime(startday.getTime());
            endday.setHours(23);
            endday.setMinutes(59);
            endday.setSeconds(59);
        }
        for (HashMap<String, PortofolioHistory> map : historyHashmapTime.values())
            singletonDataBase.appDatabase.portofolioHistoryDao().insertAll(map.values());
        Log.d(TAG,"end calcAssetLifeTime");
    }

    private void setDay_ID_Price(Date startday, Date endday, HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase) {
        for (PortofolioHistory portofolioHistory: historyHashMap.values())
        {
            portofolioHistory.day = startday.getTime();
            portofolioHistory.id = portofolioHistory.day + (portofolioHistory.asset).hashCode();
            CandleStickEntity candleStickEntity = null;
            if (!portofolioHistory.asset.equals("USDT"))
                candleStickEntity = singletonDataBase.binanceDatabase.candelStickDayDao().getByTimeAndAsset(startday.getTime(),endday.getTime(),portofolioHistory.asset+"USDT");
            if (candleStickEntity != null && candleStickEntity.close != null) {
                portofolioHistory.price = Double.parseDouble(candleStickEntity.close);
            }
            historyHashMap.put(portofolioHistory.asset,portofolioHistory);
        }
    }

    private void fillAmountFromYesterday(HashMap<String, PortofolioHistory> yesterdayHistory,HashMap<String, PortofolioHistory> historyHashMap) {
        if (yesterdayHistory != null)
        {
            Log.d(TAG,"fillAmountFromYesterday:" +yesterdayHistory.values().size());
            for (PortofolioHistory yp : yesterdayHistory.values())
            {
                PortofolioHistory portofolioHistory = new PortofolioHistory();
                portofolioHistory.asset = yp.asset;
                portofolioHistory.amount = yp.amount;
                historyHashMap.put(portofolioHistory.asset, portofolioHistory);
            }
        }
        else
            Log.d(TAG,"fillAmountFromYesterday yesterdayHistory is null");
    }

    private void addTradesForDay(Date start,Date end,HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase)
    {
        List<String> traidedPairsForDay = singletonDataBase.binanceDatabase.historyTradeDao().getTradedPairsForDay(start.getTime(),end.getTime());
        if (traidedPairsForDay != null)
        {
            for (String pair : traidedPairsForDay)
            {
                List<HistoryTrade> trades = singletonDataBase.binanceDatabase.historyTradeDao().getTraidsByDayAndName(start.getTime(),end.getTime(), pair);
                MarketPair mpair = new MarketPair(pair);
                PortofolioHistory base = getPortofolio(mpair.getBaseAsset(),start,end,historyHashMap);
                PortofolioHistory quote = getPortofolio(mpair.getQuoteAsset(),start,end,historyHashMap);
                for (HistoryTrade trade : trades)
                {
                    if (trade.buyer)
                    {
                        quote.amount += Double.parseDouble(trade.qty) -Double.parseDouble(trade.commission);
                        base.amount -= Double.parseDouble(trade.quoteQty);
                    }
                    else
                    {
                        quote.amount -= Double.parseDouble(trade.qty);
                        base.amount += Double.parseDouble(trade.quoteQty)-Double.parseDouble(trade.commission);
                    }
                }
            }
        }
    }

    private void addDepositsForDay(Date start, Date end, HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase)
    {
        List<DepositHistoryEntity> depositHistories = singletonDataBase.binanceDatabase.depositHistoryDao().getByTime(start.getTime(), end.getTime());
        for (DepositHistoryEntity entity : depositHistories)
        {
            PortofolioHistory portofolioHistory = getPortofolio(entity.asset,start,end,historyHashMap);
            portofolioHistory.amount += entity.amount;
        }
    }



    private PortofolioHistory getPortofolio(String name,Date date,Date end,HashMap<String, PortofolioHistory> historyHashMap)
    {
        PortofolioHistory portofolioHistory = historyHashMap.get(name);
        if (portofolioHistory == null)
        {
            portofolioHistory = new PortofolioHistory();
            portofolioHistory.day = date.getTime();
            portofolioHistory.asset = name;
            portofolioHistory.id = (portofolioHistory.day + portofolioHistory.asset).hashCode();
            historyHashMap.put(name,portofolioHistory);
        }
        return portofolioHistory;
    }
}
