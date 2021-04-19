package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PortofolioHistory {
    @PrimaryKey
    public long id;
    @ColumnInfo(name = "day")
    public long day;
    @ColumnInfo(name = "amount")
    public double amount;
    @ColumnInfo(name = "price")
    public double price;
    @ColumnInfo(name = "asset")
    public String asset;

}
