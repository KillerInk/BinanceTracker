package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.HistoryTrade;

import java.util.List;

@Dao
public abstract class HistoryTradeDao extends BaseDao<HistoryTrade> {

    @Query("SELECT * FROM historytrade WHERE id IN (:historyIds)")
    public abstract List<HistoryTrade> loadAllByIds(int[] historyIds);

    @Query("SELECT * FROM historytrade WHERE symbol LIKE :first")
    public abstract List<HistoryTrade> findByName(String first);

    @Query("SELECT DISTINCT symbol FROM historytrade")
    public abstract List<String> getTradedPairs();

    @Query("SELECT DISTINCT symbol FROM historytrade WHERE time BETWEEN :startTime AND :endTime")
    public abstract List<String> getTradedPairsForDay(long startTime, long endTime);

    @Query("SELECT DISTINCT * FROM historytrade WHERE time BETWEEN :startTime AND :endTime AND symbol LIKE :name")
    public abstract List<HistoryTrade> getTraidsByDayAndName(long startTime, long endTime, String name);

    @Query("SELECT * FROM historytrade WHERE time = (SELECT MAX(time) FROM historytrade) AND symbol LIKE :name")
    public abstract HistoryTrade getLastTradeBySymbol(String name);


}
