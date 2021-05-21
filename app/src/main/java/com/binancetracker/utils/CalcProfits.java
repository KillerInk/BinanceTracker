package com.binancetracker.utils;

import android.util.Log;

import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.CandleStickEntity;
import com.binancetracker.repo.room.entity.DailyAccountSnapshotEntity;
import com.binancetracker.repo.room.entity.DepositHistoryEntity;
import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;
import com.binancetracker.repo.room.entity.HistoryTradeEntity;
import com.binancetracker.repo.room.entity.InterestHistoryEntity;
import com.binancetracker.repo.room.entity.PortofolioHistory;
import com.binancetracker.repo.room.entity.Profit;
import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;

import java.util.ArrayList;
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
                    List<HistoryTradeEntity> trades = singletonDataBase.binanceDatabase.historyTradeDao().findByName(pair);
                    Profit quoteass = getQuoteAsset(mpair,assets);
                    Profit baseass = getBaseAsset(mpair,assets);

                    for (HistoryTradeEntity ht : trades)
                    {
                        if (ht.buyer)
                        {
                            quoteass.profit += Double.parseDouble(ht.qty)- Double.parseDouble(ht.commission);
                            quoteass.tradescount++;
                            baseass.profit -= Double.parseDouble(ht.quoteQty);
                        }
                        else
                        {
                            quoteass.profit -= Double.parseDouble(ht.qty);
                            baseass.profit += Double.parseDouble(ht.quoteQty) - Double.parseDouble(ht.commission);
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

                    List<FuturesTransactionHistoryEntity> futuresTransactionHistoryEntities = singletonDataBase.binanceDatabase.futuresTransactionHistoryDao().getByName(s);
                    if (futuresTransactionHistoryEntities != null && futuresTransactionHistoryEntities.size() > 0) {
                        for (FuturesTransactionHistoryEntity entity : futuresTransactionHistoryEntities) {
                            if (entity.type.equals("1") || entity.type.equals("3")) {
                                profit.profit -= Double.parseDouble(entity.amount);
                            }
                            else
                                profit.profit += Double.parseDouble(entity.amount);
                        }
                    }

                    List<InterestHistoryEntity> interestHistoryEntities = singletonDataBase.binanceDatabase.interestHistoryDao().getByName(s);
                    if (interestHistoryEntities != null && interestHistoryEntities.size() > 0) {
                        for (InterestHistoryEntity entity : interestHistoryEntities) {
                            profit.profit += entity.interest;
                        }
                    }
                }

                /*List<SwapHistoryEntity> swapHistoryEntityList = singletonDataBase.binanceDatabase.swapHistoryDao().getAll();
                if (swapHistoryEntityList != null)
                {
                    for (SwapHistoryEntity entity : swapHistoryEntityList)
                    {
                        Profit quoteAsset = singletonDataBase.appDatabase.profitDao().getByName(entity.quoteAsset);
                        Profit baseAsset = singletonDataBase.appDatabase.profitDao().getByName(entity.baseAsset);
                    }
                }*/
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
        MyTime startday = new MyTime(firstDepositTime).setDayToBegin();
        MyTime endday = new MyTime(firstDepositTime).setDayToEnd();
        MyTime finalDay = new MyTime().setDayToEnd();

        HashMap<Long, HashMap<String, PortofolioHistory>> historyHashmapTime = new HashMap<>();

        while (startday.getTime() <= finalDay.getTime()) {

            MyTime yesterday = new MyTime(startday.getTime()).setDays(-1);
            HashMap<String, PortofolioHistory> yesterdayHistory = historyHashmapTime.get(yesterday.getTime());
            HashMap<String, PortofolioHistory> historyHashMap = new HashMap<>();

            fillAmountFromYesterday(yesterdayHistory,historyHashMap);
            addDepositsForDay(startday,endday,historyHashMap,singletonDataBase);
            addFutureTransactions(startday,endday,historyHashMap,singletonDataBase);
            addTradesForDay(startday,endday,historyHashMap,singletonDataBase);
            addInterestHistory(startday,endday,historyHashMap,singletonDataBase);
            setDay_ID_Price(startday, endday, historyHashMap,singletonDataBase);

            historyHashmapTime.put(startday.getTime(), historyHashMap);

            Log.d(TAG,"Finished Day: " + startday.getString());
            startday.setDays(1).setDayToBegin();
            endday.setDays(1).setDayToEnd();
        }
        for (HashMap<String, PortofolioHistory> map : historyHashmapTime.values())
            singletonDataBase.appDatabase.portofolioHistoryDao().insertAll(map.values());
        Log.d(TAG,"end calcAssetLifeTime");
    }



    private void setDay_ID_Price(MyTime startday, MyTime endday, HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase) {
        for (PortofolioHistory portofolioHistory: historyHashMap.values())
        {
            portofolioHistory.day = startday.getTime();
            portofolioHistory.id = portofolioHistory.day + (portofolioHistory.asset).hashCode();
            long utcstartTime = startday.getUtcTime();
            long utdend = endday.getUtcTime();
            CandleStickEntity candleStickEntity = null;
            if (!portofolioHistory.asset.equals("USDT"))
                candleStickEntity = singletonDataBase.binanceDatabase.candelStickDayDao().getByTimeAndAsset(utcstartTime,utdend,portofolioHistory.asset+"USDT");
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

    private void addFutureTransactions(MyTime startday, MyTime endday, HashMap<String, PortofolioHistory> historyHashMap, SingletonDataBase singletonDataBase) {
        List<String> transactionsForDay = singletonDataBase.binanceDatabase.futuresTransactionHistoryDao().getTransactionsForDay(startday.getTime(),endday.getTime());
        if (transactionsForDay != null)
        {
            for (String asset : transactionsForDay)
            {
                List<FuturesTransactionHistoryEntity> entities = singletonDataBase.binanceDatabase.futuresTransactionHistoryDao().getTraidsByDayAndName(startday.getTime(),endday.getTime(), asset);
                for (FuturesTransactionHistoryEntity entity : entities)
                {
                    // one of 1( from spot to USDT-Ⓜ), 2( from USDT-Ⓜ to spot), 3( from spot to COIN-Ⓜ), and 4( from COIN-Ⓜ to spot)
                    PortofolioHistory portofolioHistory = getPortofolio(entity.asset,startday,endday,historyHashMap);
                    if (entity.type.equals("1") || entity.type.equals("3")) {
                        portofolioHistory.amount -= Double.parseDouble(entity.amount);
                    }
                    else
                        portofolioHistory.amount += Double.parseDouble(entity.amount);
                }
            }
        }
    }

    private void addTradesForDay(MyTime start,MyTime end,HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase)
    {
        List<String> traidedPairsForDay = singletonDataBase.binanceDatabase.historyTradeDao().getTradedPairsForDay(start.getTime(),end.getTime());
        if (traidedPairsForDay != null)
        {
            for (String pair : traidedPairsForDay)
            {
                List<HistoryTradeEntity> trades = singletonDataBase.binanceDatabase.historyTradeDao().getTraidsByDayAndName(start.getTime(),end.getTime(), pair);
                MarketPair mpair = new MarketPair(pair);
                PortofolioHistory base = getPortofolio(mpair.getBaseAsset(),start,end,historyHashMap);
                PortofolioHistory quote = getPortofolio(mpair.getQuoteAsset(),start,end,historyHashMap);
                for (HistoryTradeEntity trade : trades)
                {
                    if (trade.buyer)
                    {
                        if (!trade.commissionAsset.equals("BNB"))
                            quote.amount += Double.parseDouble(trade.qty) -Double.parseDouble(trade.commission);
                        else
                            quote.amount += Double.parseDouble(trade.qty);
                        base.amount -= Double.parseDouble(trade.quoteQty);
                        if(base.amount < 0)
                        {
                            quote.amount += base.amount / Double.parseDouble(trade.price);
                            base.amount = 0;
                        }
                    }
                    else
                    {
                        quote.amount -= Double.parseDouble(trade.qty);
                        if (!trade.commissionAsset.equals("BNB"))
                            base.amount += Double.parseDouble(trade.quoteQty)-Double.parseDouble(trade.commission);
                        else
                            base.amount += Double.parseDouble(trade.quoteQty);
                        if (quote.amount < 0)
                        {
                            base.amount += quote.amount * Double.parseDouble(trade.price);
                            quote.amount = 0;
                        }
                    }
                }
            }
        }
    }

    private void addDepositsForDay(MyTime start, MyTime end, HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase)
    {
        List<DepositHistoryEntity> depositHistories = singletonDataBase.binanceDatabase.depositHistoryDao().getByTime(start.getTime(), end.getTime());
        for (DepositHistoryEntity entity : depositHistories)
        {
            PortofolioHistory portofolioHistory = getPortofolio(entity.asset,start,end,historyHashMap);
            portofolioHistory.amount += entity.amount;
        }
    }



    private PortofolioHistory getPortofolio(String name,MyTime date,MyTime end,HashMap<String, PortofolioHistory> historyHashMap)
    {
        PortofolioHistory portofolioHistory = historyHashMap.get(name);
        if (portofolioHistory == null)
        {
            portofolioHistory = new PortofolioHistory();
            portofolioHistory.day = date.getTime();
            portofolioHistory.asset = name;
            portofolioHistory.amount = 0;
            portofolioHistory.id = portofolioHistory.day + (portofolioHistory.asset).hashCode();
            historyHashMap.put(name,portofolioHistory);
        }
        return portofolioHistory;
    }

    private void addInterestHistory(MyTime startTime,MyTime endtime,HashMap<String, PortofolioHistory> historyHashMap,SingletonDataBase singletonDataBase)
    {
        List<InterestHistoryEntity> interestHistoryEntities = singletonDataBase.binanceDatabase.interestHistoryDao().getByTime(startTime.getTime(),endtime.getTime());
        for (InterestHistoryEntity entity : interestHistoryEntities)
        {
            PortofolioHistory portofolioHistory = getPortofolio(entity.asset,startTime,endtime,historyHashMap);
            portofolioHistory.amount += entity.interest;
        }
    }

   /* public void calcAssetLifetimeAccountSnapShot(SingletonDataBase singletonDataBase)
    {
        singletonDataBase.appDatabase.portofolioHistoryDao().deleteAll();
        long firstDepositTime = singletonDataBase.binanceDatabase.dailyAccountSnapshotDao().getOldestTime();
        MyTime startday = new MyTime(firstDepositTime).setDayToBegin();
        MyTime endday = new MyTime(firstDepositTime).setDayToEnd();
        MyTime finalDay = new MyTime().setDayToEnd();
        while (startday.getTime() <= finalDay.getTime()) {

            List<DailyAccountSnapshotEntity> dailyAccountSnapshotEntities = singletonDataBase.binanceDatabase.dailyAccountSnapshotDao().getByTime(startday.getUtcTime(),endday.getUtcTime());
            List<PortofolioHistory> portofolioHistories = new ArrayList<>();
            for (DailyAccountSnapshotEntity entity : dailyAccountSnapshotEntities)
            {
                PortofolioHistory portofolioHistory = new PortofolioHistory();
                portofolioHistory.amount =
            }


            Log.d(TAG,"Finished Day: " + startday.getString());
            startday.setDays(1).setDayToBegin();
            endday.setDays(1).setDayToEnd();
        }
    }*/
}
