package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.Profit;

import java.util.List;

@Dao
public abstract class ProfitDao extends BaseDao<Profit>{

    @Query("SELECT asset FROM profit")
    public abstract List<String> getAllAssets();
    @Query("SELECT * FROM profit WHERE asset LIKE :name LIMIT 1")
    public abstract Profit getByName(String name);

}
