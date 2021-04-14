package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.binancetracker.room.entity.Profit;

import java.util.List;

@Dao
public abstract class ProfitDao extends BaseDao<Profit>{

    @Query("SELECT asset FROM profit")
    public abstract List<String> getAllAssets();

}
