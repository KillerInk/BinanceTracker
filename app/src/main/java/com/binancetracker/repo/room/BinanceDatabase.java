package com.binancetracker.repo.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.repo.room.dao.CandelStickDayDao;
import com.binancetracker.repo.room.dao.DepositHistoryDao;
import com.binancetracker.repo.room.dao.FuturesTransactionHistoryDao;
import com.binancetracker.repo.room.dao.HistoryTradeDao;
import com.binancetracker.repo.room.dao.MarketDao;
import com.binancetracker.repo.room.dao.WithdrawHistoryDao;
import com.binancetracker.repo.room.entity.CandleStickEntity;
import com.binancetracker.repo.room.entity.DepositHistoryEntity;
import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;
import com.binancetracker.repo.room.entity.HistoryTrade;
import com.binancetracker.repo.room.entity.Market;
import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;

@Database(entities =
        {
                Market.class,
                HistoryTrade.class,
                DepositHistoryEntity.class,
                WithdrawHistoryEntity.class,
                CandleStickEntity.class,
                FuturesTransactionHistoryEntity.class
        },
        version = 2)
public abstract class BinanceDatabase extends RoomDatabase {
    public abstract MarketDao marketDao();
    public abstract HistoryTradeDao historyTradeDao();
    public abstract DepositHistoryDao depositHistoryDao();
    public abstract WithdrawHistoryDao withdrawHistoryDao();
    public abstract CandelStickDayDao candelStickDayDao();
    public abstract FuturesTransactionHistoryDao futuresTransactionHistoryDao();
}

