package com.binancetracker.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.room.dao.AssetModelDao;
import com.binancetracker.room.dao.CandelStickDayDao;
import com.binancetracker.room.dao.DepositHistoryDao;
import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.dao.PortofolioHistoryDao;
import com.binancetracker.room.dao.ProfitDao;
import com.binancetracker.room.dao.WithdrawHistoryDao;
import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.room.entity.DepositHistoryEntity;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;
import com.binancetracker.room.entity.PortofolioHistory;
import com.binancetracker.room.entity.Profit;
import com.binancetracker.room.entity.WithdrawHistoryEntity;
import com.binancetracker.ui.main.AssetModel;

@Database(entities =
        {
                Profit.class,
                AssetModel.class,
                PortofolioHistory.class
        },
        version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfitDao profitDao();
    public abstract AssetModelDao assetModelDao();
    public abstract PortofolioHistoryDao portofolioHistoryDao();
}
