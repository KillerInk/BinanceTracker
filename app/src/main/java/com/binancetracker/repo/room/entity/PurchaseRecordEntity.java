package com.binancetracker.repo.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PurchaseRecordEntity {
    @PrimaryKey
    public long id;
    @ColumnInfo(name ="amount")
    public String amount;
    @ColumnInfo(name ="asset")
    public String asset;
    @ColumnInfo(name ="creatTime")
    public Long creatTime;
    @ColumnInfo(name ="lot")
    public Long lot;
    @ColumnInfo(name ="productName")
    public String productName;
    @ColumnInfo(name ="purchaseId")
    public Long purchaseId;
    @ColumnInfo(name ="status")
    public String status;

}
