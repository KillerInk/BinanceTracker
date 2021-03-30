package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.binancetracker.room.entity.DespositHistoryEntity;

import java.util.List;

@Dao
public interface DespositHistoryDao {
    @Query("SELECT * FROM DespositHistoryEntity")
    List<DespositHistoryEntity> getAll();

    @Query("SELECT * FROM DespositHistoryEntity WHERE asset LIKE :first")
    List<DespositHistoryEntity> findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DespositHistoryEntity market);

    @Delete
    void delete(DespositHistoryEntity market);

}
