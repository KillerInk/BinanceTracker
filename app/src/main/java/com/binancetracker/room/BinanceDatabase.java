package com.binancetracker.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.room.dao.AssetModelDao;
import com.binancetracker.room.dao.CandelStickDayDao;
import com.binancetracker.room.dao.DepositHistoryDao;
import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.dao.ProfitDao;
import com.binancetracker.room.dao.WithdrawHistoryDao;
import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.room.entity.DepositHistoryEntity;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;
import com.binancetracker.room.entity.Profit;
import com.binancetracker.room.entity.WithdrawHistoryEntity;
import com.binancetracker.ui.main.AssetModel;

@Database(entities =
        {
                Market.class,
                HistoryTrade.class,
                DepositHistoryEntity.class,
                WithdrawHistoryEntity.class,
                CandleStickEntity.class
        },
        version = 1)
public abstract class BinanceDatabase extends RoomDatabase {
    public abstract MarketDao marketDao();
    public abstract HistoryTradeDao historyTradeDao();
    public abstract DepositHistoryDao depositHistoryDao();
    public abstract WithdrawHistoryDao withdrawHistoryDao();
    public abstract CandelStickDayDao candelStickDayDao();
}

