package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InterestHistoryEntity {
    @PrimaryKey
    public long id;
    @ColumnInfo(name ="asset")
    public String asset;
    @ColumnInfo(name ="interest")
    public Double interest;
    @ColumnInfo(name ="lendingType")
    public String lendingType;
    @ColumnInfo(name ="productName")
    public String productName;
    @ColumnInfo(name ="time")
    public Long time;
}
