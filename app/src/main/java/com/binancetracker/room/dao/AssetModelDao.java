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
public abstract class AssetModelDao extends BaseDao<AssetModel> {

    @Query("SELECT asset FROM assetmodel")
    public abstract List<String> getAllAssets();
}
