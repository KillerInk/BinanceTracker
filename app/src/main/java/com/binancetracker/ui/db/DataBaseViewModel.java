package com.binancetracker.ui.db;

import android.os.Handler;
import android.os.Looper;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.binancetracker.api.BinanceApi;
import com.binancetracker.api.DownloadTradeHistory;
import com.binancetracker.utils.CalcProfits;


public class DataBaseViewModel extends ViewModel
{
    public ObservableField<String> dbstatus = new ObservableField<>();
    //public MutableLiveData<String> dbstatus = new MutableLiveData<>();
    private int max;

    public void startSyncTradeHistory()
    {
        dbstatus.set("StartSync");
        new Thread(new Runnable() {
            private Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void run() {
                BinanceApi.getInstance().getDownloadTradeHistory().setHistoryEvent(new DownloadTradeHistory.TradeHistoryEvent() {
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
                BinanceApi.getInstance().getDownloadTradeHistory().getFullHistory();
            }
        }).start();
    }

    public void calcTrades()
    {
        CalcProfits profits = new CalcProfits();
        profits.calcProfits();
    }

}
