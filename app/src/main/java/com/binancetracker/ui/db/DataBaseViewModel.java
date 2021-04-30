package com.binancetracker.ui.db;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.api.BinanceApi;
import com.binancetracker.repo.api.runnable.ClientFactoryRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.CalcProfits;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DataBaseViewModel extends ViewModel
{
    public ObservableField<String> dbstatus = new ObservableField<>();
    public ObservableField<String> errorMSG = new ObservableField<>();
    private int max;
    private BinanceApi binanceApi;
    private SingletonDataBase singletonDataBase;

    @Inject
    public DataBaseViewModel(BinanceApi binanceApi,SingletonDataBase singletonDataBase)
    {
        this.binanceApi = binanceApi;
        this.singletonDataBase = singletonDataBase;
    }

    public void startSyncTradeHistory()
    {
        dbstatus.set("StartSync");
        binanceApi.getDownloadTradeHistory().setMessageEventListner(messageEvent);
        //start downloading the trade history
        binanceApi.getDownloadTradeHistory().getFullHistory();
    }

    public void startSyncDeposits()
    {
        binanceApi.getDownloadDespositHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadDespositHistory().downloadFullHistory();
    }

    public void startSyncWithdraws()
    {
        binanceApi.getDownloadWithdrawHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadWithdrawHistory().downloadFullHistory();
    }

    public void startDownloadPriceHistoryDayFull()
    {
        binanceApi.getDownloadCandleStickHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadCandleStickHistory().downloadFullHistory();
    }

    public void startDownloadFuturesTransactionHistory()
    {
        binanceApi.getDownloadFuturesHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadFuturesHistory().downloadFullHistory();
    }

    public void startDownloadSwapHistory()
    {
        binanceApi.getDownloadSwapHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadSwapHistory().downloadSwapHistory();
    }

    public void startDownloadLiquidSwapHistory()
    {
        binanceApi.getDownloadSwapHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadSwapHistory().downloadLiquidSwapHistory();
    }

    public void startDownloadSavingPurchaseHistory()
    {
        binanceApi.getDownloadSavingHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadSavingHistory().downloadPurchaseHistory();
    }

    public void startDownloadSavingRedemptionHistory()
    {
        binanceApi.getDownloadSavingHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadSavingHistory().downloadRedemptionHistory();
    }

    public void startDownloadSavingInterestHistory()
    {
        binanceApi.getDownloadSavingHistory().setMessageEventListner(messageEvent);
        binanceApi.getDownloadSavingHistory().downloadInterestHistory();
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
        binanceApi.getDownloadCandleStickHistory().setMessageEventListner(null);
        binanceApi.getDownloadWithdrawHistory().setMessageEventListner(null);
        binanceApi.getDownloadDespositHistory().setMessageEventListner(null);
        binanceApi.getDownloadTradeHistory().setMessageEventListner(null);
        binanceApi.getDownloadFuturesHistory().setMessageEventListner(null);
        binanceApi.getDownloadSwapHistory().setMessageEventListner(null);
        binanceApi.getDownloadSavingHistory().setMessageEventListner(null);
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
