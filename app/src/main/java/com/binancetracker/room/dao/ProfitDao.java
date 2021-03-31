package com.binancetracker.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.binancetracker.room.entity.Profit;

import java.util.List;

@Dao
public interface ProfitDao {

    @Query("SELECT * FROM profit")
    List<Profit> getAll();

    @Insert
    void insert(Profit profit);

    @Delete
    void delete(Profit profit);

    @Query("DELETE FROM profit")
    void delete();
}
