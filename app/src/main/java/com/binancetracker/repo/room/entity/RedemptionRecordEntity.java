package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RedemptionRecordEntity
{
    @ColumnInfo(name ="amount")
    public String amount;
    @ColumnInfo(name ="asset")
    public String asset;
    @PrimaryKey
    public Long createTime;
    @ColumnInfo(name ="interest")
    public Double interest;
    @ColumnInfo(name ="principal")
    public Double principal;
    @ColumnInfo(name ="projectId")
    public String projectId;
    @ColumnInfo(name ="projectName")
    public String projectName;
    @ColumnInfo(name ="status")
    public String status;
}
