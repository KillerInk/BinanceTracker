package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.binance.api.client.domain.account.Trade;
import com.binancetracker.repo.room.dao.HistoryTradeDao;

@Entity
public class HistoryTradeEntity {

    /**
     * Trade id.
     */
    @PrimaryKey
    public Long id;

    /**
     * Price.
     */
    @ColumnInfo(name = "price")
    public String price;

    /**
     * Quantity.
     */
    @ColumnInfo(name = "qty")
    public String qty;


    /**
     * Quote quantity for the trade (price * qty).
     */
    @ColumnInfo(name = "quoteQty")
    public String quoteQty;

    /**
     * Commission.
     */
    @ColumnInfo(name = "commission")
    public String commission;

    /**
     * Asset on which commission is taken
     */
    @ColumnInfo(name = "commissionAsset")
    public String commissionAsset;

    /**
     * Trade execution time.
     */
    @ColumnInfo(name = "time")
    public long time;

    /**
     * The symbol of the trade.
     */
    @ColumnInfo(name = "symbol")
    public String symbol;

    @ColumnInfo(name = "isBuyer")
    public boolean buyer;

    @ColumnInfo(name = "isMaker")
    public boolean maker;
}
