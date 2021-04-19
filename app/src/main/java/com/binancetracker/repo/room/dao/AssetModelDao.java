package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.ui.main.AssetModel;

import java.util.List;

@Dao
public abstract class AssetModelDao extends BaseDao<AssetModel> {

    @Query("SELECT asset FROM assetmodel")
    public abstract List<String> getAllAssets();
}
