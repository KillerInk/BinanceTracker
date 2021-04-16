package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.room.entity.PortofolioHistory;
import com.binancetracker.ui.main.AssetModel;

import java.util.Collection;
import java.util.List;

@Dao
public abstract class PortofolioHistoryDao extends BaseDao<PortofolioHistory>
{


    @Query("SELECT * FROM PortofolioHistory WHERE day BETWEEN :startDate AND :endDate AND asset LIKE :assetn LIMIT 1")
    public abstract PortofolioHistory getByTimeAndAsset(Long startDate, Long endDate, String assetn);

    @Query("SELECT * FROM PortofolioHistory WHERE day BETWEEN :startDate AND :endDate")
    public abstract List<PortofolioHistory> getByTimeRange(Long startDate,Long endDate);
}
