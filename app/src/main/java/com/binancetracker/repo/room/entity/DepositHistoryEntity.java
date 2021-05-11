package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DepositHistoryEntity {

    /**
     * Trade id.
     */
    @PrimaryKey
    public Long id;

    /**
     * Amount deposited.
     */
    @ColumnInfo(name = "amount")
    public double amount;

    /**
     * Symbol.
     */
    @ColumnInfo(name = "asset")
    public String asset;

    /**
     * Deposit time.
     */
    @ColumnInfo(name = "insertTime")
    public long insertTime;

    /**
     * Transaction id
     */
    @ColumnInfo(name = "txId")
    public String txId;

}
