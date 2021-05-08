package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.Market;

import java.util.List;

@Dao
public abstract class MarketDao extends BaseDao<Market> {

    @Query("SELECT * FROM market WHERE uid IN (:marketIds)")
    public abstract List<Market> loadAllByIds(int[] marketIds);

    @Query("SELECT * FROM market WHERE baseAsset LIKE :first AND " +
            "quoteAsset LIKE :last LIMIT 1")
    public abstract Market findByName(String first, String last);


    @Query("SELECT * FROM market WHERE baseAsset LIKE :first")
    public abstract List<Market> findByAsset(String first);
}
