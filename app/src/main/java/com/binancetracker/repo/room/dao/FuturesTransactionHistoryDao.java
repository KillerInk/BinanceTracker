package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;

import java.util.List;

@Dao
public abstract class FuturesTransactionHistoryDao extends BaseDao<FuturesTransactionHistoryEntity> {
    @Query("SELECT DISTINCT asset FROM FuturesTransactionHistoryEntity WHERE timestamp BETWEEN :startTime AND :endTime")
    public abstract List<String> getTransactionsForDay(long startTime, long endTime);

    @Query("SELECT DISTINCT * FROM FuturesTransactionHistoryEntity WHERE timestamp BETWEEN :startTime AND :endTime AND asset LIKE :name")
    public abstract List<FuturesTransactionHistoryEntity> getTraidsByDayAndName(long startTime, long endTime, String name);

    @Query("SELECT DISTINCT * FROM FuturesTransactionHistoryEntity WHERE asset LIKE :name")
    public abstract List<FuturesTransactionHistoryEntity> getByName(String name);


}
