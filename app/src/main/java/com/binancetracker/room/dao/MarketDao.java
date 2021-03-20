package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.binancetracker.room.entity.Market;

import java.util.List;

@Dao
public interface MarketDao {
    @Query("SELECT * FROM market")
    List<Market> getAll();

    @Query("SELECT * FROM market WHERE uid IN (:marketIds)")
    List<Market> loadAllByIds(int[] marketIds);

    @Query("SELECT * FROM market WHERE baseAsset LIKE :first AND " +
            "quoteAsset LIKE :last LIMIT 1")
    Market findByName(String first, String last);

    @Insert
    void insert(Market market);

    @Delete
    void delete(Market market);

}