package com.binancetracker.repo.api.runnable.staking;

import com.binance.api.client.api.sync.BinanceApiStakingRestClient;
import com.binance.api.client.domain.staking.StakingPosition;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binance.api.client.factory.BinanceStakingApiClientFactory;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.AssetModel;

import java.util.HashMap;
import java.util.List;

public class DownloadStakingPositionsRunner extends ClientFactoryRunner<BinanceStakingApiClientFactory> {

    public interface DownloadEvent
    {
        void downloadFinished(List<StakingPosition> positionList);
    }

    private DownloadEvent downloadEventListner;

    public DownloadStakingPositionsRunner(BinanceStakingApiClientFactory clientFactory, SingletonDataBase singletonDataBase) {
        super(clientFactory, singletonDataBase);
    }


    public void setDownloadEventListner(DownloadEvent downloadEventListner) {
        this.downloadEventListner = downloadEventListner;
    }

    @Override
    public void processRun() {
        BinanceApiStakingRestClient client = clientFactory.newRestClient();
        List<StakingPosition> positionList = client.getPositionsStaking("STAKING",null,null,null,null,null);
        if (downloadEventListner != null)
            downloadEventListner.downloadFinished(positionList);
    }
}
