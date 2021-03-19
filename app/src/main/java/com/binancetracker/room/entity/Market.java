package com.binancetracker.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Market {

    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "symbol")
    public String symbol;
    /*@ColumnInfo(name = "status")
    public SymbolStatus status;*/
    @ColumnInfo(name = "baseAsset")
    public String baseAsset;
    @ColumnInfo(name = "baseAssetPrecision")
    public Integer baseAssetPrecision;
    @ColumnInfo(name = "quoteAsset")
    public String quoteAsset;
    @ColumnInfo(name = "quotePrecision")
    public Integer quotePrecision;

    //public List<OrderType> orderTypes;
    @ColumnInfo(name = "icebergAllowed")
    public boolean icebergAllowed;
    @ColumnInfo(name = "ocoAllowed")
    public boolean ocoAllowed;
    @ColumnInfo(name = "quoteOrderQtyMarketAllowed")
    public boolean quoteOrderQtyMarketAllowed;
    @ColumnInfo(name = "isSpotTradingAllowed")
    public boolean isSpotTradingAllowed;
    @ColumnInfo(name = "isMarginTradingAllowed")
    public boolean isMarginTradingAllowed;

}
