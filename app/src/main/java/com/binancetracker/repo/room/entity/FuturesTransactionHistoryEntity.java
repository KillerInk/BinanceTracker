package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FuturesTransactionHistoryEntity {
    @PrimaryKey
    public long id;
    @ColumnInfo(name = "asset")
    public String asset;
    @ColumnInfo(name = "tranId")
    public long tranId;
    @ColumnInfo(name = "amount")
    public String amount;
    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "timestamp")
    public long timestamp;
    @ColumnInfo(name = "status")
    public String status;

}
