package com.binancetracker.ui.db;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.api.runnable.account.DownloadDailyAccountSnapshotSpot;
import com.binancetracker.repo.api.runnable.account.DownloadDepositFullHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadFullDayHistoryForAllPairsRunner;
import com.binancetracker.repo.api.runnable.futures.DownloadFuturesTransactionHistory;
import com.binancetracker.repo.api.runnable.staking.DownloadStakingPositionsRunner;
import com.binancetracker.repo.api.runnable.swap.DownloadLiquidSwapHistoryRunner;
import com.binancetracker.repo.api.runnable.saving.DownloadSavingInterestHistoryRunner;
import com.binancetracker.repo.api.runnable.saving.DownloadSavingPurchaseHistoryRunner;
import com.binancetracker.repo.api.runnable.saving.DownloadSavingRedemptionHistoryRunner;
import com.binancetracker.repo.api.runnable.swap.DownloadSwapHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadTradeFullHistoryRunner;
import com.binancetracker.repo.api.runnable.account.DownloadWithdrawFullHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.thread.RestExecuter;
import com.binancetracker.utils.CalcProfits;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DataBaseViewModel extends ViewModel
{
    public ObservableField<String> dbstatus = new ObservableField<>();
    public ObservableField<String> errorMSG = new ObservableField<>();
    private int max;

    private SingletonDataBase singletonDataBase;
    private DownloadWithdrawFullHistory downloadWithdrawFullHistory;
    private DownloadTradeFullHistoryRunner downloadTradeFullHistoryRunner;
    private DownloadDepositFullHistoryRunner downloadDepositFullHistoryRunner;
    private DownloadFullDayHistoryForAllPairsRunner downloadFullDayHistoryForAllPairsRunner;
    private DownloadFuturesTransactionHistory downloadFuturesTransactionHistory;
    private DownloadSwapHistoryRunner downloadSwapHistoryRunner;
    private DownloadLiquidSwapHistoryRunner downloadLiquidSwapHistoryRunner;
    private DownloadSavingRedemptionHistoryRunner downloadSavingRedemptionHistoryRunner;
    private DownloadSavingPurchaseHistoryRunner downloadSavingPurchaseHistoryRunner;
    private DownloadSavingInterestHistoryRunner downloadSavingInterestHistoryRunner;
    private DownloadDailyAccountSnapshotSpot downloadDailyAccountSnapshotSpot;
    private DownloadStakingPositionsRunner downloadStakingPositionsRunner;

    @Inject
    public DataBaseViewModel(
                             SingletonDataBase singletonDataBase,
                             DownloadWithdrawFullHistory downloadWithdrawFullHistory,
                             DownloadTradeFullHistoryRunner downloadTradeFullHistoryRunner,
                             DownloadDepositFullHistoryRunner downloadDepositFullHistoryRunner,
                             DownloadFullDayHistoryForAllPairsRunner downloadFullDayHistoryForAllPairsRunner,
                             DownloadFuturesTransactionHistory downloadFuturesTransactionHistory,
                             DownloadSwapHistoryRunner downloadSwapHistoryRunner,
                             DownloadLiquidSwapHistoryRunner downloadLiquidSwapHistoryRunner,
                             DownloadSavingRedemptionHistoryRunner downloadSavingRedemptionHistoryRunner,
                             DownloadSavingPurchaseHistoryRunner downloadSavingPurchaseHistoryRunner,
                             DownloadSavingInterestHistoryRunner downloadSavingInterestHistoryRunner,
                             DownloadDailyAccountSnapshotSpot downloadDailyAccountSnapshotSpot,
                             DownloadStakingPositionsRunner downloadStakingPositionsRunner)
    {
        this.singletonDataBase = singletonDataBase;
        this.downloadWithdrawFullHistory = downloadWithdrawFullHistory;
        this.downloadTradeFullHistoryRunner = downloadTradeFullHistoryRunner;
        this.downloadDepositFullHistoryRunner = downloadDepositFullHistoryRunner;
        this.downloadFullDayHistoryForAllPairsRunner = downloadFullDayHistoryForAllPairsRunner;
        this.downloadFuturesTransactionHistory = downloadFuturesTransactionHistory;
        this.downloadSwapHistoryRunner = downloadSwapHistoryRunner;
        this.downloadLiquidSwapHistoryRunner = downloadLiquidSwapHistoryRunner;
        this.downloadSavingInterestHistoryRunner = downloadSavingInterestHistoryRunner;
        this.downloadSavingPurchaseHistoryRunner = downloadSavingPurchaseHistoryRunner;
        this.downloadSavingRedemptionHistoryRunner = downloadSavingRedemptionHistoryRunner;
        this.downloadDailyAccountSnapshotSpot = downloadDailyAccountSnapshotSpot;
        this.downloadStakingPositionsRunner = downloadStakingPositionsRunner;
    }

    public void clearDBs()
    {
        RestExecuter.addTask(new Runnable() {
            @Override
            public void run() {
                singletonDataBase.clearDBs();
            }
        });

    }

    public void startSyncTradeHistory()
    {
        dbstatus.set("StartSync");
        downloadTradeFullHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadTradeFullHistoryRunner);
    }

    public void startSyncDeposits()
    {
        downloadDepositFullHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadDepositFullHistoryRunner);
    }

    public void startSyncWithdraws()
    {
        downloadWithdrawFullHistory.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadWithdrawFullHistory);
    }

    public void startDownloadPriceHistoryDayFull()
    {
        downloadFullDayHistoryForAllPairsRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadFullDayHistoryForAllPairsRunner);
    }

    public void startDownloadFuturesTransactionHistory()
    {
        downloadFuturesTransactionHistory.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadFuturesTransactionHistory);
    }

    public void startDownloadSwapHistory()
    {
        downloadSwapHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadSwapHistoryRunner);
    }

    public void startDownloadLiquidSwapHistory()
    {
        downloadLiquidSwapHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadLiquidSwapHistoryRunner);
    }

    public void startDownloadSavingPurchaseHistory()
    {
        downloadSavingPurchaseHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadSavingPurchaseHistoryRunner);
    }

    public void startDownloadSavingRedemptionHistory()
    {
        downloadSavingRedemptionHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadSavingRedemptionHistoryRunner);
    }

    public void startDownloadSavingInterestHistory()
    {
        downloadSavingInterestHistoryRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadSavingInterestHistoryRunner);
    }

    public void startDownloadDailyAccountSnapshotSpot()
    {
        downloadDailyAccountSnapshotSpot.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadDailyAccountSnapshotSpot);
    }

    public void startDownloadStakingPositions()
    {
        downloadStakingPositionsRunner.setMessageEventListner(messageEvent);
        RestExecuter.addTask(downloadStakingPositionsRunner);
    }

    public void calcTrades()
    {
        CalcProfits profits = new CalcProfits();
        profits.calcProfits(singletonDataBase);
    }

    public void calcLifeTimeHistory()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new CalcProfits().calcAssetLifeTimeHistory(singletonDataBase);
            }
        }).start();
    }


    private void resetMessageEvent()
    {
        this.downloadWithdrawFullHistory.setMessageEventListner(null);
        this.downloadTradeFullHistoryRunner.setMessageEventListner(null);
        this.downloadDepositFullHistoryRunner.setMessageEventListner(null);
        this.downloadFullDayHistoryForAllPairsRunner.setMessageEventListner(null);
        this.downloadFuturesTransactionHistory.setMessageEventListner(null);
        this.downloadSwapHistoryRunner.setMessageEventListner(null);
        this.downloadLiquidSwapHistoryRunner.setMessageEventListner(null);
        this.downloadSavingInterestHistoryRunner.setMessageEventListner(null);
        this.downloadSavingPurchaseHistoryRunner.setMessageEventListner(null);
        this.downloadSavingRedemptionHistoryRunner.setMessageEventListner(null);
        this.downloadStakingPositionsRunner.setMessageEventListner(null);
    }

    private ClientFactoryRunner.MessageEvent messageEvent = new ClientFactoryRunner.MessageEvent() {

        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void onSyncStart(int max_markets) {
            max = max_markets;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dbstatus.set("0/"+max);
                }
            });
        }

        @Override
        public void onSyncUpdate(int currentmarket,String msg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dbstatus.set(currentmarket+"/"+max+" "+msg);
                }
            });

        }

        @Override
        public void onSyncEnd() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dbstatus.set("Done Sync");
                    resetMessageEvent();
                }
            });
        }

        @Override
        public void onError(String msg) {
            dbstatus.set("Sync failed: " + msg);
            errorMSG.set(msg);
            resetMessageEvent();
        }
    };


}
