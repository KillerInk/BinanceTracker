package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.binancetracker.room.entity.HistoryTrade;

import java.util.List;

@Dao
public interface HistoryTradeDao {
    @Query("SELECT * FROM historytrade")
    List<HistoryTrade> getAll();

    @Query("SELECT * FROM historytrade WHERE id IN (:historyIds)")
    List<HistoryTrade> loadAllByIds(int[] historyIds);

    @Query("SELECT * FROM historytrade WHERE symbol LIKE :first")
    List<HistoryTrade> findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HistoryTrade market);

    @Delete
    void delete(HistoryTrade market);

    @Query("SELECT DISTINCT symbol FROM historytrade")
    List<String> getTradedPairs();

    @Query("SELECT * FROM historytrade WHERE time = (SELECT MAX(time) FROM historytrade) AND symbol LIKE :name")
    HistoryTrade getLastTradeBySymbol(String name);


}
