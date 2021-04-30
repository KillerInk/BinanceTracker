package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.binance.api.client.domain.account.SwapHistory;

@Entity
public class SwapHistoryEntity {
    @PrimaryKey
    public Long swapId;
    @ColumnInfo(name ="swapTime")
    public Long swapTime;
    @ColumnInfo(name ="status")
    public Integer status;
    @ColumnInfo(name ="quoteAsset")
    public String quoteAsset;
    @ColumnInfo(name ="baseAsset")
    public String baseAsset;
    @ColumnInfo(name ="quoteQty")
    public double quoteQty;
    @ColumnInfo(name ="baseQty")
    public double baseQty;
    @ColumnInfo(name ="price")
    public double price;
    @ColumnInfo(name ="fee")
    public double fee;


}
