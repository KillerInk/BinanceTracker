package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binance.api.client.domain.saving.InterestHistory;
import com.binancetracker.repo.room.entity.CandleStickEntity;
import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;
import com.binancetracker.repo.room.entity.InterestHistoryEntity;

import java.util.List;

@Dao
public abstract class InterestHistoryDao extends BaseDao<InterestHistoryEntity> {

    @Query("SELECT DISTINCT * FROM InterestHistoryEntity WHERE asset LIKE :name")
    public abstract List<InterestHistoryEntity> getByName(String name);

    @Query("SELECT * FROM InterestHistoryEntity WHERE time BETWEEN :startDate AND :endDate")
    public abstract List<InterestHistoryEntity> getByTime(Long startDate, Long endDate);
}
