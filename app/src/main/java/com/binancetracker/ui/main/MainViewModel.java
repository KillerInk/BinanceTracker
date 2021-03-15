package com.binancetracker.ui.main;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binance.api.client.domain.account.AssetBalance;
import com.binancetracker.api.AccountBalance;
import com.binancetracker.api.BinanceApi;


public class MainViewModel extends ViewModel implements AccountBalance.AccountBalanceEvent {

    public MutableLiveData<AssetModel[]> balances = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public void onResume()
    {
        BinanceApi.getInstance().getAccountBalance().setAccountBalanceEventListner(this::onBalanceChanged);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BinanceApi.getInstance().getAccountBalance().startListenToAssetBalance();
            }
        }).start();

    }

    public void onPause()
    {
        BinanceApi.getInstance().getAccountBalance().setAccountBalanceEventListner(null);
        BinanceApi.getInstance().getAccountBalance().stopListenToAssetBalance();
    }

    @Override
    public void onBalanceChanged() {
        AssetBalance[] assets = BinanceApi.getInstance().getAccountBalance().getAccountBalanceCache().values().toArray(new AssetBalance[BinanceApi.getInstance().getAccountBalance().getAccountBalanceCache().values().size()]);
        int i = 0;
        AssetModel[] ret = new AssetModel[assets.length];
        for (AssetBalance assetBalance : assets)
            ret[i++] = new AssetModel(assetBalance);
        handler.post(new Runnable() {
            @Override
            public void run() {
                balances.setValue(ret);
            }
        });
    }
}