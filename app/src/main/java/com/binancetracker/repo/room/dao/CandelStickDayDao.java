package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.CandleStickEntity;

import java.util.List;

@Dao
public abstract class CandelStickDayDao extends BaseDao<CandleStickEntity> {

    @Query("SELECT symbol FROM candlestickentity")
    public abstract List<String> getAllAssets();

    @Query("SELECT * FROM CandleStickEntity WHERE openTime BETWEEN :startDate AND :endDate AND symbol LIKE :assetn LIMIT 1")
    public abstract CandleStickEntity getByTimeAndAsset(Long startDate, Long endDate,String assetn);

    @Query("SELECT openTime FROM candlestickentity WHERE openTime = (SELECT MAX(openTime) FROM candlestickentity) LIMIT 1")
    public abstract long getLatestDate();
}
