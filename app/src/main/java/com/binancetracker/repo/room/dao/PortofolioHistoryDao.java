package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.PortofolioHistory;

import java.util.List;

@Dao
public abstract class PortofolioHistoryDao extends BaseDao<PortofolioHistory>
{

    @Query("SELECT * FROM PortofolioHistory WHERE day BETWEEN :startDate AND :endDate AND asset LIKE :assetn LIMIT 1")
    public abstract PortofolioHistory getByTimeAndAsset(Long startDate, Long endDate, String assetn);

    @Query("SELECT * FROM PortofolioHistory WHERE day BETWEEN :startDate AND :endDate")
    public abstract List<PortofolioHistory> getByTimeRange(Long startDate,Long endDate);

    @Query("SELECT day FROM portofoliohistory WHERE day = (SELECT MIN(day) FROM portofoliohistory) LIMIT 1")
    public abstract long getOldestTime();
}
