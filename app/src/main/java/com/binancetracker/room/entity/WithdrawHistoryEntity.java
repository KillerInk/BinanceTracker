package com.binancetracker.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WithdrawHistoryEntity {

    /**
     * Amount withdrawn.
     */
    @ColumnInfo(name = "amount")
    public double amount;

    /**
     * Destination address.
     */
    @ColumnInfo(name = "address")
    public String address;

    /**
     * Symbol.
     */
    @ColumnInfo(name = "asset")
    public String asset;
    @ColumnInfo(name = "applyTime")
    public Long applyTime;
    @ColumnInfo(name = "successTime")
    public Long successTime;

    /**
     * Transaction id.
     */
    @ColumnInfo(name = "txId")
    public String txId;

    /**
     * Id.
     */
    @PrimaryKey
    public Long id;
}
