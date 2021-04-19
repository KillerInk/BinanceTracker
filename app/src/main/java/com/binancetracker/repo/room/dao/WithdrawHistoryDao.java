package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;

import java.util.List;

@Dao
public abstract class WithdrawHistoryDao extends BaseDao<WithdrawHistoryEntity> {
    @Query("SELECT * FROM WithdrawHistoryEntity WHERE asset LIKE :first")
    public abstract List<WithdrawHistoryEntity> findByName(String first);
}
