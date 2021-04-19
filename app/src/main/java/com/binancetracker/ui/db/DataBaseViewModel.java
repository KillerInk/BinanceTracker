package com.binancetracker.ui.db;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.binancetracker.repo.api.BinanceApi;
import com.binancetracker.repo.api.DownloadTradeHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.CalcProfits;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DataBaseViewModel extends ViewModel
{
    public ObservableField<String> dbstatus = new ObservableField<>();
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
        new Thread(new Runnable() {
            private Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void run() {
                binanceApi.getDownloadTradeHistory().setHistoryEvent(new DownloadTradeHistory.TradeHistoryEvent() {
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
                    public void onSyncUpdate(int currentmarket) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dbstatus.set(currentmarket+"/"+max);
                            }
                        });

                    }

                    @Override
                    public void onSyncEnd() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dbstatus.set("Done Sync");
                            }
                        });
                    }
                });
                //start downloading the trade history
                binanceApi.getDownloadTradeHistory().getFullHistory();
            }
        }).start();
    }

    public void startSyncDeposits()
    {
        binanceApi.getDownloadDespositHistory().downloadFullHistory();
    }

    public void startSyncWithdraws()
    {
        binanceApi.getDownloadWithdrawHistory().downloadFullHistory();
    }

    public void startDownloadPriceHistoryDayFull()
    {
        binanceApi.getDownloadCandleStickHistory().downloadFullHistory();
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



}
