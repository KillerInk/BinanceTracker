package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.binance.api.client.domain.LiquidityOperationRecordStatus;

@Entity
public class LiquidityOperationRecordEntity {

    @PrimaryKey
    public Long operationId;
    @ColumnInfo(name ="poolId")
    public Long poolId;
    @ColumnInfo(name ="poolName")
    public String poolName;
    @ColumnInfo(name ="operation")
    public String operation;
    @ColumnInfo(name ="status")
    public String status;
    @ColumnInfo(name ="updateTime")
    public Long updateTime;
    @ColumnInfo(name ="shareAmount")
    public String shareAmount;
}
