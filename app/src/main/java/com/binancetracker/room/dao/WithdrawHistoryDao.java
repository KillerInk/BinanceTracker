package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.binancetracker.room.entity.WithdrawHistoryEntity;

import java.util.List;

@Dao
public abstract class WithdrawHistoryDao extends BaseDao<WithdrawHistoryEntity> {
    @Query("SELECT * FROM WithdrawHistoryEntity WHERE asset LIKE :first")
    public abstract List<WithdrawHistoryEntity> findByName(String first);
}
