package com.binancetracker.repo.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.binancetracker.repo.room.dao.CandelStickDayDao;
import com.binancetracker.repo.room.dao.DepositHistoryDao;
import com.binancetracker.repo.room.dao.FuturesTransactionHistoryDao;
import com.binancetracker.repo.room.dao.HistoryTradeDao;
import com.binancetracker.repo.room.dao.InterestHistoryDao;
import com.binancetracker.repo.room.dao.LiquidityOperationRecordDao;
import com.binancetracker.repo.room.dao.MarketDao;
import com.binancetracker.repo.room.dao.PurchaseRecordDao;
import com.binancetracker.repo.room.dao.RedemptionRecordDao;
import com.binancetracker.repo.room.dao.SwapHistoryDao;
import com.binancetracker.repo.room.dao.WithdrawHistoryDao;
import com.binancetracker.repo.room.entity.CandleStickEntity;
import com.binancetracker.repo.room.entity.DepositHistoryEntity;
import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;
import com.binancetracker.repo.room.entity.HistoryTrade;
import com.binancetracker.repo.room.entity.InterestHistoryEntity;
import com.binancetracker.repo.room.entity.LiquidityOperationRecordEntity;
import com.binancetracker.repo.room.entity.Market;
import com.binancetracker.repo.room.entity.PurchaseRecordEntity;
import com.binancetracker.repo.room.entity.RedemptionRecordEntity;
import com.binancetracker.repo.room.entity.SwapHistoryEntity;
import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;

@Database(entities =
        {
                Market.class,
                HistoryTrade.class,
                DepositHistoryEntity.class,
                WithdrawHistoryEntity.class,
                CandleStickEntity.class,
                FuturesTransactionHistoryEntity.class,
                SwapHistoryEntity.class,
                LiquidityOperationRecordEntity.class,
                RedemptionRecordEntity.class,
                PurchaseRecordEntity.class,
                InterestHistoryEntity.class
        },
        version = 4)
public abstract class BinanceDatabase extends RoomDatabase {
    public abstract MarketDao marketDao();
    public abstract HistoryTradeDao historyTradeDao();
    public abstract DepositHistoryDao depositHistoryDao();
    public abstract WithdrawHistoryDao withdrawHistoryDao();
    public abstract CandelStickDayDao candelStickDayDao();
    public abstract FuturesTransactionHistoryDao futuresTransactionHistoryDao();
    public abstract SwapHistoryDao swapHistoryDao();
    public abstract LiquidityOperationRecordDao liquidityOperationRecordDao();
    public abstract RedemptionRecordDao redemptionRecordDao();
    public abstract PurchaseRecordDao purchaseRecordDao();
    public abstract InterestHistoryDao interestHistoryDao();
}

