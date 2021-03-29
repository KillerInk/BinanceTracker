package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.binancetracker.ui.main.AssetModel;

import java.util.Collection;
import java.util.List;

@Dao
public interface AssetModelDao {

    @Query("SELECT * FROM assetmodel")
    List<AssetModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AssetModel assetModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Collection<AssetModel> users);

    @Delete
    void delete(AssetModel assetModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(AssetModel assetModel);
}
