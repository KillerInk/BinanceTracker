package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.binancetracker.room.entity.WithdrawHistoryEntity;

import java.util.List;

@Dao
public interface WithdrawHistoryDao {
    @Query("SELECT * FROM WithdrawHistoryEntity")
    List<WithdrawHistoryEntity> getAll();

    @Query("SELECT * FROM WithdrawHistoryEntity WHERE asset LIKE :first")
    List<WithdrawHistoryEntity> findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WithdrawHistoryEntity market);

    @Delete
    void delete(WithdrawHistoryEntity market);
}
