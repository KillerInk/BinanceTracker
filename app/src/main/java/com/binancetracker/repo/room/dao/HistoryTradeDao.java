package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.HistoryTradeEntity;

import java.util.List;

@Dao
public abstract class HistoryTradeDao extends BaseDao<HistoryTradeEntity> {

    @Query("SELECT * FROM HistoryTradeEntity WHERE id IN (:historyIds)")
    public abstract List<HistoryTradeEntity> loadAllByIds(int[] historyIds);

    @Query("SELECT * FROM HistoryTradeEntity WHERE symbol LIKE :first ")
    public abstract List<HistoryTradeEntity> findByName(String first);

    @Query("SELECT DISTINCT symbol FROM HistoryTradeEntity")
    public abstract List<String> getTradedPairs();

    @Query("SELECT DISTINCT symbol FROM HistoryTradeEntity WHERE time BETWEEN :startTime AND :endTime ORDER BY time ASC")
    public abstract List<String> getTradedPairsForDay(long startTime, long endTime);

    @Query("SELECT DISTINCT * FROM HistoryTradeEntity WHERE time BETWEEN :startTime AND :endTime AND symbol LIKE :name ORDER BY time ASC")
    public abstract List<HistoryTradeEntity> getTraidsByDayAndName(long startTime, long endTime, String name);

    @Query("SELECT * FROM HistoryTradeEntity WHERE time = (SELECT MAX(time) FROM HistoryTradeEntity) AND symbol LIKE :name")
    public abstract HistoryTradeEntity getLastTradeBySymbol(String name);


}
