package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CandleStickEntity {
    @PrimaryKey()
    public Long id;
    @ColumnInfo(name = "openTime")
    public Long openTime;
    @ColumnInfo(name = "symbol")
    public String symbol;
    @ColumnInfo(name = "open")
    public String open;
    @ColumnInfo(name = "high")
    public String high;
    @ColumnInfo(name = "low")
    public String low;
    @ColumnInfo(name = "close")
    public String close;
    @ColumnInfo(name = "volume")
    public String volume;
    @ColumnInfo(name = "closeTime")
    public Long closeTime;
    @ColumnInfo(name = "quoteAssetVolume")
    public String quoteAssetVolume;
    @ColumnInfo(name = "numberOfTrades")
    public Long numberOfTrades;
    @ColumnInfo(name = "takerBuyBaseAssetVolume")
    public String takerBuyBaseAssetVolume;
    @ColumnInfo(name = "takerBuyQuoteAssetVolume")
    public String takerBuyQuoteAssetVolume;
}
