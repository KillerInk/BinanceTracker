package com.binancetracker.repo.api.runnable;

import com.binance.api.client.domain.account.Deposit;
import com.binance.api.client.domain.account.FuturesTransactionHistory;
import com.binance.api.client.domain.swap.LiquidityOperationRecord;
import com.binance.api.client.domain.swap.SwapHistory;
import com.binance.api.client.domain.account.Withdraw;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.saving.InterestHistory;
import com.binance.api.client.domain.saving.PurchaseRecord;
import com.binance.api.client.domain.saving.RedemptionRecord;
import com.binancetracker.repo.room.entity.CandleStickEntity;
import com.binancetracker.repo.room.entity.DepositHistoryEntity;
import com.binancetracker.repo.room.entity.FuturesTransactionHistoryEntity;
import com.binancetracker.repo.room.entity.InterestHistoryEntity;
import com.binancetracker.repo.room.entity.LiquidityOperationRecordEntity;
import com.binancetracker.repo.room.entity.Market;
import com.binancetracker.repo.room.entity.PurchaseRecordEntity;
import com.binancetracker.repo.room.entity.RedemptionRecordEntity;
import com.binancetracker.repo.room.entity.SwapHistoryEntity;
import com.binancetracker.repo.room.entity.WithdrawHistoryEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonToDBConverter {

    public static Market getMarketEntity(SymbolInfo i) {
        Market market = new Market();
        market.baseAsset = i.getBaseAsset();
        market.baseAssetPrecision = i.getBaseAssetPrecision();
        market.icebergAllowed = i.isIcebergAllowed();
        market.isMarginTradingAllowed = i.isMarginTradingAllowed();
        market.isSpotTradingAllowed = i.isSpotTradingAllowed();
        market.ocoAllowed = i.isOcoAllowed();
        market.quoteAsset = i.getQuoteAsset();
        market.quotePrecision = i.getQuotePrecision();
        market.quoteOrderQtyMarketAllowed = i.isQuoteOrderQtyMarketAllowed();
        //market.status = i.getStatus();
        market.symbol = i.getSymbol();
        return market;
    }


    public static SwapHistoryEntity getSwapHistoryEntity(SwapHistory s) {
        SwapHistoryEntity entity = new SwapHistoryEntity();
        entity.baseAsset = s.getBaseAsset();
        entity.baseQty = s.getBaseQty();
        entity.fee = s.getFee();
        entity.price = s.getPrice();
        entity.quoteAsset = s.getQuoteAsset();
        entity.status = s.getStatus();
        entity.quoteQty = s.getQuoteQty();
        entity.swapId = s.getSwapId();
        entity.swapTime = s.getSwapTime();
        return entity;
    }

    public static LiquidityOperationRecordEntity getLiquidityOperationRecordEntity(LiquidityOperationRecord record) {
        LiquidityOperationRecordEntity entity = new LiquidityOperationRecordEntity();
        entity.id = record.getPoolName().hashCode() + record.getUpdateTime();
        entity.shareAmount = record.getShareAmount();
        entity.operation = record.getOperation();
        entity.operationId = record.getOperationId();
        entity.poolId = record.getPoolId();
        entity.poolName = record.getPoolName();
        entity.status = record.getStatus().name();
        entity.updateTime = record.getUpdateTime();
        return entity;
    }

    public static WithdrawHistoryEntity getWithdrawHistoryEntity(Withdraw deposit) {
        WithdrawHistoryEntity dhe = new WithdrawHistoryEntity();
        dhe.id = (long)deposit.getApplyTime().hashCode();
        dhe.asset = deposit.getAsset();
        dhe.amount = Double.parseDouble(deposit.getAmount());
        //'2020-12-23 19:38:57'
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = f.parse(deposit.getApplyTime());
            dhe.applyTime = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (deposit.getSuccessTime() != null)
            dhe.successTime = Long.parseLong(deposit.getSuccessTime());
        if (deposit.getAddress() != null)
            dhe.address = deposit.getAddress();
        if (deposit.getTxId() != null)
            dhe.txId = deposit.getTxId();
        return dhe;
    }

    public static PurchaseRecordEntity getPurchaseEntity(PurchaseRecord record)
    {
        PurchaseRecordEntity redemptionRecordEntity = new PurchaseRecordEntity();
        redemptionRecordEntity.id = record.getPurchaseId() + record.getAsset().hashCode();
        redemptionRecordEntity.amount = record.getAmount();
        redemptionRecordEntity.asset = record.getAsset();
        if (record.getCreatTime() != null)
            redemptionRecordEntity.creatTime = record.getCreatTime();
        redemptionRecordEntity.lot = record.getLot();
        redemptionRecordEntity.productName = record.getProductName();
        redemptionRecordEntity.purchaseId = record.getPurchaseId();
        redemptionRecordEntity.status =record.getStatus();
        return redemptionRecordEntity;
    }

    public static RedemptionRecordEntity getRedemptionEntity(RedemptionRecord record)
    {
        RedemptionRecordEntity redemptionRecordEntity = new RedemptionRecordEntity();
        //redemptionRecordEntity.id = record.getPurchaseId() + record.getAsset().hashCode();
        redemptionRecordEntity.amount = record.getAmount();
        redemptionRecordEntity.asset = record.getAsset();
        if (record.getCreateTime() != null)
            redemptionRecordEntity.createTime = record.getCreateTime();
        redemptionRecordEntity.principal = record.getPrincipal();
        redemptionRecordEntity.projectName = record.getProjectName();
        redemptionRecordEntity.interest = record.getInterest();
        redemptionRecordEntity.projectId = record.getProjectId();
        redemptionRecordEntity.status =record.getStatus();
        return redemptionRecordEntity;
    }

    public static FuturesTransactionHistoryEntity getFuturesTransactionHistoryEntity(FuturesTransactionHistory transactionHistory) {
        FuturesTransactionHistoryEntity entity = new FuturesTransactionHistoryEntity();
        entity.id = transactionHistory.getAsset().hashCode() + transactionHistory.getTimestamp();
        entity.amount = transactionHistory.getAmount();
        entity.asset = transactionHistory.getAsset();
        entity.status = transactionHistory.getStatus();
        entity.timestamp = transactionHistory.getTimestamp();
        entity.tranId = transactionHistory.getTranId();
        entity.type = transactionHistory.getType();
        return entity;
    }

    public static CandleStickEntity getCandleStickEntity(Candlestick candlestick, String symbol) {
        CandleStickEntity candleStickEntity = new CandleStickEntity();
        candleStickEntity.close = candlestick.getClose();
        candleStickEntity.closeTime = candlestick.getCloseTime();
        candleStickEntity.high = candlestick.getHigh();
        candleStickEntity.low = candlestick.getLow();
        candleStickEntity.numberOfTrades = candlestick.getNumberOfTrades();
        candleStickEntity.open = candlestick.getOpen();
        candleStickEntity.openTime = candlestick.getOpenTime();
        candleStickEntity.quoteAssetVolume = candlestick.getQuoteAssetVolume();
        candleStickEntity.takerBuyBaseAssetVolume = candlestick.getTakerBuyBaseAssetVolume();
        candleStickEntity.takerBuyQuoteAssetVolume = candlestick.getTakerBuyQuoteAssetVolume();
        candleStickEntity.symbol =symbol;
        candleStickEntity.volume = candlestick.getVolume();
        candleStickEntity.id = (long)(symbol).hashCode()+candlestick.getOpenTime();
        return candleStickEntity;
    }

    public static DepositHistoryEntity getDepositHistoryEntity(Deposit deposit) {
        DepositHistoryEntity dhe = new DepositHistoryEntity();
        dhe.id = Long.parseLong(deposit.getInsertTime())+deposit.getAsset().hashCode();
        dhe.asset = deposit.getAsset();
        dhe.amount = Double.parseDouble(deposit.getAmount());
        dhe.insertTime = Long.parseLong(deposit.getInsertTime());
        dhe.txId = deposit.getTxId();
        return dhe;
    }

    public static InterestHistoryEntity getInterestHistoryEntity(InterestHistory history)
    {
        InterestHistoryEntity entity = new InterestHistoryEntity();
        entity.asset = history.getAsset();
        entity.id = history.getAsset().hashCode() +history.getTime();
        entity.interest = history.getInterest();
        entity.lendingType = history.getLendingType();
        entity.productName = history.getProductName();
        entity.time = history.getTime();
        return entity;
    }
}
