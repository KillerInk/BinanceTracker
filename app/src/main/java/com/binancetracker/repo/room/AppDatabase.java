package com.binancetracker.repo.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.repo.room.dao.AssetModelDao;
import com.binancetracker.repo.room.dao.PortofolioHistoryDao;
import com.binancetracker.repo.room.dao.ProfitDao;
import com.binancetracker.repo.room.entity.PortofolioHistory;
import com.binancetracker.repo.room.entity.Profit;
import com.binancetracker.repo.room.entity.AssetModel;

@Database(entities =
        {
                Profit.class,
                AssetModel.class,
                PortofolioHistory.class
        },
        version = 10)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfitDao profitDao();
    public abstract AssetModelDao assetModelDao();
    public abstract PortofolioHistoryDao portofolioHistoryDao();

    public void clearDBs()
    {
        profitDao().deleteAll();
        assetModelDao().deleteAll();
        portofolioHistoryDao().deleteAll();
    }
}
