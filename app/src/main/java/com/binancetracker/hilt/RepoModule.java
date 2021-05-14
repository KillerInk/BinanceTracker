package com.binancetracker.hilt;

import android.content.Context;

import com.binance.api.client.factory.BinanceAbstractFactory;
import com.binance.api.client.factory.BinanceSavingApiClientFactory;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binance.api.client.factory.BinanceSwapApiClientFactory;
import com.binancetracker.repo.AssetRepo;
import com.binancetracker.repo.api.AccountBalance;
import com.binancetracker.repo.api.Ticker;
import com.binancetracker.repo.api.runnable.account.Download30DaysDepositHistoryRunner;
import com.binancetracker.repo.api.runnable.account.Download30DaysWithdrawHistoryRunner;
import com.binancetracker.repo.api.runnable.account.DownloadDepositFullHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadFullDayHistoryForAllPairsRunner;
import com.binancetracker.repo.api.runnable.futures.DownloadFuturesTransactionHistory;
import com.binancetracker.repo.api.runnable.market.DownloadLastTradeHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadLatestDayHistoryForAllPairsRunner;
import com.binancetracker.repo.api.runnable.swap.DownloadLiquidSwapHistoryRunner;
import com.binancetracker.repo.api.runnable.saving.DownloadSavingInterestHistoryRunner;
import com.binancetracker.repo.api.runnable.saving.DownloadSavingPurchaseHistoryRunner;
import com.binancetracker.repo.api.runnable.saving.DownloadSavingRedemptionHistoryRunner;
import com.binancetracker.repo.api.runnable.swap.DownloadSwapHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadTradeFullHistoryRunner;
import com.binancetracker.repo.api.runnable.account.DownloadWithdrawFullHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepoModule
{

    @Provides
    @Singleton
    public static AssetRepo assetRepo(Settings settings,
                                      SingletonDataBase singletonDataBase,
                                      AccountBalance accountBalance,
                                      DownloadLastTradeHistoryRunner downloadLastTradeHistoryRunner,
                                      Download30DaysDepositHistoryRunner download30DaysDepositHistoryRunner,
                                      Download30DaysWithdrawHistoryRunner download30DaysWithdrawHistoryRunner,
                                      DownloadLatestDayHistoryForAllPairsRunner downloadLatestDayHistoryForAllPairsRunner,
                                      Ticker ticker)
    {
        return new AssetRepo(settings,
                singletonDataBase,
                accountBalance,
                downloadLastTradeHistoryRunner,
                download30DaysDepositHistoryRunner,
                download30DaysWithdrawHistoryRunner,
                downloadLatestDayHistoryForAllPairsRunner,
                ticker);
    }



    @Provides
    @Singleton
    public static Settings settings(@ApplicationContext Context context)
    {
        return new Settings(context);
    }


    @Provides
    @Singleton
    public static SingletonDataBase singletonDataBase(@ApplicationContext Context context)
    {
        return new SingletonDataBase(context);
    }

    @Provides
    public static BinanceSpotApiClientFactory spotApiClientFactory(Settings settings)
    {
        return BinanceAbstractFactory.createSpotFactory(settings.getKEY(), settings.getSECRETKEY());
    }

    @Provides
    public static BinanceSwapApiClientFactory swapApiClientFactory(Settings settings)
    {
        return BinanceAbstractFactory.createSwapFactory(settings.getKEY(), settings.getSECRETKEY());
    }

    @Provides
    public static BinanceSavingApiClientFactory savingApiClientFactory(Settings settings)
    {
        return BinanceAbstractFactory.createSavingFactory(settings.getKEY(), settings.getSECRETKEY());
    }

    @Provides
    public static DownloadWithdrawFullHistory downloadWithdrawFullHistory(BinanceSpotApiClientFactory clientFactory,SingletonDataBase singletonDataBase)
    {
        return new DownloadWithdrawFullHistory(clientFactory,singletonDataBase);
    }

    @Provides
    public static Download30DaysWithdrawHistoryRunner downloadWithdraw30DayHistory(BinanceSpotApiClientFactory clientFactory,SingletonDataBase singletonDataBase)
    {
        return new Download30DaysWithdrawHistoryRunner(clientFactory,singletonDataBase);
    }


    @Provides
    public static DownloadTradeFullHistoryRunner downloadTradeFullHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadTradeFullHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadLastTradeHistoryRunner downloadLastTradeHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadLastTradeHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadDepositFullHistoryRunner downloadDepositFullHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadDepositFullHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static Download30DaysDepositHistoryRunner download30DaysDepositHistoryRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new Download30DaysDepositHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadFullDayHistoryForAllPairsRunner downloadFullDayHistoryForAllPairsRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase,Settings settings)
    {
        return new DownloadFullDayHistoryForAllPairsRunner(clientFactory,singletonDataBase,settings);
    }

    @Provides
    public static DownloadLatestDayHistoryForAllPairsRunner downloadLatestDayHistoryForAllPairsRunner(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase,Settings settings)
    {
        return new DownloadLatestDayHistoryForAllPairsRunner(clientFactory,singletonDataBase,settings);
    }

    @Provides
    public static DownloadFuturesTransactionHistory downloadFuturesTransactionHistory(BinanceSpotApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadFuturesTransactionHistory(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadSwapHistoryRunner downloadSwapHistoryRunner(BinanceSwapApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadSwapHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadLiquidSwapHistoryRunner downloadLiquidSwapHistoryRunner(BinanceSwapApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadLiquidSwapHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadSavingRedemptionHistoryRunner downloadSavingRedemptionHistoryRunner(BinanceSavingApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadSavingRedemptionHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadSavingPurchaseHistoryRunner downloadSavingPurchaseHistoryRunner(BinanceSavingApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadSavingPurchaseHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static DownloadSavingInterestHistoryRunner downloadSavingInterestHistoryRunner(BinanceSavingApiClientFactory clientFactory, SingletonDataBase singletonDataBase)
    {
        return new DownloadSavingInterestHistoryRunner(clientFactory,singletonDataBase);
    }

    @Provides
    public static AccountBalance accountBalance(BinanceSpotApiClientFactory clientFactory)
    {
        return new AccountBalance(clientFactory);
    }

    @Provides
    public static Ticker ticker(BinanceSpotApiClientFactory clientFactory)
    {
        return new Ticker(clientFactory);
    }

}
