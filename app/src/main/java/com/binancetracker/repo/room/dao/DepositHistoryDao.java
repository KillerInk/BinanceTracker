package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.DepositHistoryEntity;

import java.util.List;

@Dao
public abstract class DepositHistoryDao extends BaseDao<DepositHistoryEntity> {

    @Query("SELECT insertTime FROM DepositHistoryEntity ORDER BY insertTime ASC LIMIT 1")
    public abstract long getFirstDepositTime();

    @Query("SELECT * FROM DepositHistoryEntity WHERE insertTime BETWEEN :startDate AND :endDate")
    public abstract List<DepositHistoryEntity> getByTime(Long startDate,Long endDate);

    @Query("SELECT * FROM DepositHistoryEntity WHERE asset LIKE :first")
    public abstract List<DepositHistoryEntity> findByName(String first);
}
