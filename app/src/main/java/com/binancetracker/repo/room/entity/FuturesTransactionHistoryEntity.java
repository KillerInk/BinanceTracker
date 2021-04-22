package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FuturesTransactionHistoryEntity {
    @ColumnInfo(name = "asset")
    public String asset;
    @ColumnInfo(name = "tranId")
    public long tranId;
    @ColumnInfo(name = "amount")
    public String amount;
    @ColumnInfo(name = "type")
    public String type;
    @PrimaryKey
    public long timestamp;
    @ColumnInfo(name = "status")
    public String status;
}
