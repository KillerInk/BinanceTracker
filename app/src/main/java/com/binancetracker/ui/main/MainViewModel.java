package com.binancetracker.ui.main;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.general.Asset;
import com.binancetracker.api.AccountBalance;
import com.binancetracker.api.BinanceApi;
import com.binancetracker.api.Ticker;
import com.binancetracker.utils.Settings;

import java.util.ArrayList;


public class MainViewModel extends ViewModel implements AccountBalance.AccountBalanceEvent {

    private static final String TAG = MainViewModel.class.getSimpleName();
    public MutableLiveData<AssetModel[]> balances = new MutableLiveData<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private final String base = "USDT";
    private String choosenAsset;

    public void onResume()
    {
        choosenAsset = Settings.getInstance().getDefaultAsset();
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
        BinanceApi.getInstance().getTicker().setPriceChangedEvent(null);
        BinanceApi.getInstance().getTicker().stop();
    }

    @Override
    public void onBalanceChanged() {
        AssetBalance[] assets = BinanceApi.getInstance().getAccountBalance().getAccountBalanceCache().values().toArray(new AssetBalance[BinanceApi.getInstance().getAccountBalance().getAccountBalanceCache().values().size()]);
        int i = 0;
        AssetModel[] ret = new AssetModel[assets.length];
        for (AssetBalance assetBalance : assets) {
            AssetModel a =new AssetModel(assetBalance);
            a.setChoosenAsset(choosenAsset);
            ret[i++] = a;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                balances.setValue(ret);
                updatedPrices();
            }
        });
    }



    private void updatedPrices()
    {
        Log.d(TAG,"updatedPrices");
        AssetModel[] assetModels = balances.getValue();
        if (assetModels != null && assetModels.length > 0)
        {
            String markets ="";
            for (AssetModel assetModel:assetModels) {

                if (!assetModel.getAssetName().equals(base))
                    markets += assetModel.getAssetName()+base+",";
            }
            if (!markets.contains(choosenAsset+base))
                markets += choosenAsset+base;
            if (markets.endsWith(","))
                markets = markets.substring(0, markets.length() - 1);
            Log.d(TAG,"subscribe to markets:" + markets);
            BinanceApi.getInstance().getTicker().setMarketsToListen(markets);
            BinanceApi.getInstance().getTicker().setPriceChangedEvent(new Ticker.PriceChangedEvent() {
                @Override
                public void onPriceChanged(String symbol,final double price) {
                    //Log.d(TAG,"onPriceChanged:" + symbol + " " + price);
                    AssetModel[] assetModels = balances.getValue();
                    if (assetModels != null && assetModels.length > 0)
                    {
                        for (AssetModel assetModel:assetModels) {
                            //set usdt price
                            if (symbol.contains(assetModel.getAssetName()) && !assetModel.getAssetName().equals(base)) {
                                assetModel.setPrice(price);
                            }
                            //set choosen asset price like eur
                            if (symbol.equals(choosenAsset+base) && !assetModel.getAssetName().equals(choosenAsset))
                                assetModel.setChoosenAssetPrice(price);
                        }
                    }
                }
            });
            BinanceApi.getInstance().getTicker().start();
        }
    }
}