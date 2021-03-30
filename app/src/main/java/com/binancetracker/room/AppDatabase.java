package com.binancetracker.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.room.dao.AssetModelDao;
import com.binancetracker.room.dao.DepositHistoryDao;
import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.dao.ProfitDao;
import com.binancetracker.room.entity.DepositHistoryEntity;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;
import com.binancetracker.room.entity.Profit;
import com.binancetracker.ui.main.AssetModel;

@Database(entities = {Market.class, HistoryTrade.class, Profit.class, AssetModel.class, DepositHistoryEntity.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarketDao marketDao();
    public abstract HistoryTradeDao historyTradeDao();
    public abstract ProfitDao profitDao();
    public abstract AssetModelDao assetModelDao();
    public abstract DepositHistoryDao depositHistoryDao();
}

