package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Profit {

    /**
     * Trade id.
     */
    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "asset")
    public String asset;

    @ColumnInfo(name = "profit")
    public double profit;

    @ColumnInfo(name = "tradescount")
    public long tradescount;

    @ColumnInfo(name = "deposits")
    public double deposits;

    @ColumnInfo(name = "withdraws")
    public double withdraws;


}
