package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InterestHistoryEntity {
    @ColumnInfo(name ="asset")
    public String asset;
    @ColumnInfo(name ="interest")
    public Double interest;
    @ColumnInfo(name ="lendingType")
    public String lendingType;
    @ColumnInfo(name ="productName")
    public String productName;
    @PrimaryKey
    Long time;
}
