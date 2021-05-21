package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyAccountSnapshotEntity {

    @PrimaryKey
    public long id;
    @ColumnInfo(name = "time")
    public long time;
    /**
     * Asset symbol.
     */
    @ColumnInfo(name = "asset")
    public String asset;
    @ColumnInfo(name = "spot_price")
    public double spot_price;

    @ColumnInfo(name = "spot_free")
    public double spot_free;
    @ColumnInfo(name = "spot_locked")
    public double spot_locked;
    @ColumnInfo(name = "margin_borrowed")
    public double margin_borrowed;
    /**
     * Available balance.
     */
    @ColumnInfo(name = "margin_free")
    public double margin_free;
    @ColumnInfo(name = "margin_interest")
    public double margin_interest;

    /**
     * Locked by open orders.
     */
    @ColumnInfo(name = "margin_locked")
    public double margin_locked;
    @ColumnInfo(name = "margin_netAsset")
    public double margin_netAsset;
    @ColumnInfo(name = "futures_marginBalance")
    public double futures_marginBalance;
    @ColumnInfo(name = "futures_walletBalance")
    public double futures_walletBalance;

}
