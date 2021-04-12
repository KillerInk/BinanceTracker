package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.binancetracker.room.entity.CandleStickEntity;
import com.binancetracker.ui.main.AssetModel;

import java.util.Collection;
import java.util.List;

@Dao
public interface CandelStickDayDao {
    @Query("SELECT * FROM candlestickentity")
    List<CandleStickEntity> getAll();

    @Query("SELECT symbol FROM candlestickentity")
    List<String> getAllAssets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CandleStickEntity assetModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Collection<CandleStickEntity> users);

    @Delete
    void delete(CandleStickEntity assetModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(CandleStickEntity assetModel);
}
