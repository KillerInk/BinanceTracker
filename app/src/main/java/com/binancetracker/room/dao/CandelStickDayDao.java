package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.room.entity.DepositHistoryEntity;
import com.binancetracker.ui.main.AssetModel;

import java.util.Collection;
import java.util.List;

@Dao
public abstract class CandelStickDayDao extends BaseDao<CandleStickEntity> {

    @Query("SELECT symbol FROM candlestickentity")
    public abstract List<String> getAllAssets();

    @Query("SELECT * FROM DepositHistoryEntity WHERE insertTime BETWEEN :startDate AND :endDate AND asset LIKE :assetn LIMIT 1")
    public abstract CandleStickEntity getByTimeAndAsset(Long startDate, Long endDate,String assetn);
}
