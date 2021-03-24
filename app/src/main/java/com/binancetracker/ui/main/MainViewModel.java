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
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.Profit;
import com.binancetracker.utils.MarketPair;
import com.binancetracker.utils.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class MainViewModel extends ViewModel implements AccountBalance.AccountBalanceEvent {

    private static final String TAG = MainViewModel.class.getSimpleName();
    public MutableLiveData<Collection<AssetModel>> balances;
    private Handler handler;
    private final String base = "USDT";
    private String choosenAsset;
    private HashMap<String,AssetModel> assetModelHashMap;
    public final PieChartModel pieChartModel;

    public MainViewModel()
    {
        assetModelHashMap = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
        balances = new MutableLiveData<>();
        pieChartModel = new PieChartModel();
    }

    public void onResume()
    {
        if (Settings.getInstance().getSECRETKEY().equals("") || Settings.getInstance().getKEY().equals(""))
            return;
        choosenAsset = Settings.getInstance().getDefaultAsset();

        BinanceApi.getInstance().getAccountBalance().setAccountBalanceEventListner(this::onBalanceChanged);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getProfitsFromDb();
                applyHasmapToLiveData();
                BinanceApi.getInstance().getAccountBalance().startListenToAssetBalance();
                applyHasmapToLiveData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updatedPrices();
                    }
                }).start();
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
        for (AssetBalance assetBalance : BinanceApi.getInstance().getAccountBalance().getAccountBalanceCache().values()) {
            AssetModel a = getAssetModel(assetBalance.getAsset());
            a.setAccountBalance(assetBalance);
            a.setChoosenAsset(choosenAsset);
        }
    }

    private void getProfitsFromDb()
    {
        List<Profit> profitList = SingletonDataBase.appDatabase.profitDao().getAll();
        if (profitList != null)
        {
            for (Profit profit : profitList)
            {
                AssetModel assetModel = getAssetModel(profit.asset);
                assetModel.setProfit(profit.profit);
                assetModel.setTradescount(profit.tradescount);
            }
            //applyHasmapToLiveData();

        }
    }

    private void applyHasmapToLiveData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                balances.setValue(assetModelHashMap.values());
            }
        });
    }

    private AssetModel getAssetModel(String symbol)
    {
        AssetModel assetModel = assetModelHashMap.get(symbol);
        if (assetModel == null) {
            Log.d(TAG, "add new Asset:" + symbol);
            assetModel = new AssetModel();
            assetModel.setAssetName(symbol);
            assetModel.setChoosenAsset(Settings.getInstance().getDefaultAsset());
            assetModelHashMap.put(symbol,assetModel);
        }
        return assetModel;
    }

    private int updatePieChartcounter = 0;
    private void updatedPrices()
    {
        Log.d(TAG,"updatedPrices");
        if (assetModelHashMap.values().size() > 0)
        {
            String markets ="";
            for (AssetModel assetModel:assetModelHashMap.values()) {
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
                    AssetModel assetModel = getAssetModel(new MarketPair(symbol).getQuoteAsset());
                    if (symbol.contains(assetModel.getAssetName())) {
                        assetModel.setPrice(price);
                    }
                    //set choosen asset price like eur
                    if (symbol.equals(choosenAsset+base) && assetModel.getAssetName().equals(choosenAsset)) {
                        for (AssetModel a : assetModelHashMap.values())
                            a.setChoosenAssetPrice(price);
                        //
                    }
                    if (updatePieChartcounter++ == 100) {
                        updatePieChartcounter = 0;
                        pieChartModel.setPieChartData(assetModelHashMap);
                    }
                }
            });
            BinanceApi.getInstance().getTicker().start();
        }
    }
}