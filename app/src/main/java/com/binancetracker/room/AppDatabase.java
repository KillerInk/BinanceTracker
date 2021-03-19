package com.binancetracker.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.room.dao.HistoryTradeDao;
import com.binancetracker.room.dao.MarketDao;
import com.binancetracker.room.entity.HistoryTrade;
import com.binancetracker.room.entity.Market;

@Database(entities = {Market.class, HistoryTrade.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarketDao marketDao();
    public abstract HistoryTradeDao historyTradeDao();
}

