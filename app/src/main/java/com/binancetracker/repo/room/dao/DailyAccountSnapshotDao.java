package com.binancetracker.repo.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.binancetracker.repo.room.entity.DailyAccountSnapshotEntity;

import java.util.List;

@Dao
public abstract class DailyAccountSnapshotDao extends BaseDao<DailyAccountSnapshotEntity> {

    @Query("SELECT time FROM DailyAccountSnapshotEntity ORDER BY time ASC LIMIT 1")
    public abstract long getOldestTime();

    @Query("SELECT * FROM DailyAccountSnapshotEntity WHERE time BETWEEN :startDate AND :endDate")
    public abstract List<DailyAccountSnapshotEntity> getByTime(Long startDate, Long endDate);

    @Query("SELECT asset FROM DailyAccountSnapshotEntity")
    public abstract List<String> getAllAssets();
}
