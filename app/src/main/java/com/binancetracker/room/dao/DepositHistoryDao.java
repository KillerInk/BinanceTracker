package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.binancetracker.room.entity.DepositHistoryEntity;

import java.util.List;

@Dao
public interface DepositHistoryDao {
    @Query("SELECT * FROM DepositHistoryEntity")
    List<DepositHistoryEntity> getAll();

    @Query("SELECT insertTime FROM DepositHistoryEntity ORDER BY insertTime ASC LIMIT 1")
    long getFirstDepositTime();

    @Query("SELECT * FROM DepositHistoryEntity WHERE asset LIKE :first")
    List<DepositHistoryEntity> findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DepositHistoryEntity market);

    @Delete
    void delete(DepositHistoryEntity market);

}
