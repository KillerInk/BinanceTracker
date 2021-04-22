package com.binancetracker.repo.api.runnable;

import com.binance.api.client.api.sync.BinanceApiSwapRestClient;
import com.binance.api.client.domain.account.LiquidityOperationRecord;
import com.binance.api.client.factory.BinanceSwapApiClientFactory;
import com.binancetracker.repo.api.DownloadSwapHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.LiquidityOperationRecordEntity;
import com.binancetracker.utils.MyTime;

import java.util.ArrayList;
import java.util.List;

public class DownloadLiquidSwapHistoryRunner extends ClientFactoryRunner<BinanceSwapApiClientFactory> {
    public DownloadLiquidSwapHistoryRunner(BinanceSwapApiClientFactory binanceSwapApiClientFactory, SingletonDataBase singletonDataBase) {
        super(binanceSwapApiClientFactory, singletonDataBase);
    }

    @Override
    public void run() {
        fireOnSyncStart(0);
        BinanceApiSwapRestClient client = clientFactory.newRestClient();
        MyTime  startTime = new MyTime(System.currentTimeMillis());
        startTime.setDays(-365 * 3);
        List<LiquidityOperationRecord> liquidityOperationRecords = client.getPoolLiquidityOperationRecords(null,null,null,startTime.getTime(),System.currentTimeMillis(),100L);
        if (liquidityOperationRecords != null)
        {
            List<LiquidityOperationRecordEntity> entities = new ArrayList<>();
            for (LiquidityOperationRecord record: liquidityOperationRecords)
            {
                LiquidityOperationRecordEntity entity = new LiquidityOperationRecordEntity();
                entity.shareAmount = record.getShareAmount();
                entity.operation = record.getOperation();
                entity.operationId = record.getOperationId();
                entity.poolId = record.getPoolId();
                entity.poolName = record.getPoolName();
                entity.status = record.getStatus().name();
                entity.updateTime = record.getUpdateTime();
                entities.add(entity);
            }
            singletonDataBase.binanceDatabase.liquidityOperationRecordDao().insertAll(entities);
        }
        fireOnSyncEnd();
    }
}
