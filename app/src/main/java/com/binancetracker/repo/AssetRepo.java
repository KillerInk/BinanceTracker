package com.binancetracker.repo;

import android.util.Log;

import com.binance.api.client.domain.account.AssetBalance;
import com.binancetracker.api.AccountBalance;
import com.binancetracker.api.BinanceApi;
import com.binancetracker.api.Ticker;
import com.binancetracker.room.SingletonDataBase;
import com.binancetracker.room.entity.Profit;
import com.binancetracker.ui.main.AssetModel;
import com.binancetracker.utils.MarketPair;
import com.binancetracker.utils.Settings;

import java.util.HashMap;
import java.util.List;

public class AssetRepo implements AccountBalance.AccountBalanceEvent {

    public interface AssetEvent
    {
        void onAssetChanged();
    }


    private final String TAG = AssetRepo.class.getSimpleName();
    private HashMap<String,AssetModel> assetModelHashMap;
    private final String base = "USDT";
    private String choosenAsset;
    private AssetEvent assetEventListner;

    public AssetRepo()
    {
        assetModelHashMap = new HashMap<>();
    }

    public HashMap<String, AssetModel> getAssetModelHashMap() {
        return assetModelHashMap;
    }

    public void setAssetEventListner(AssetEvent assetEventListner) {
        this.assetEventListner = assetEventListner;
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
                getAssetModelsFromDB();
                fireAssetChangedEvent();
                getProfitsFromDb();
                fireAssetChangedEvent();
                BinanceApi.getInstance().getAccountBalance().startListenToAssetBalance();
                fireAssetChangedEvent();
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
        saveAssetModelsToDB();
    }

    private void fireAssetChangedEvent()
    {
        if (assetEventListner != null)
            assetEventListner.onAssetChanged();
    }

    @Override
    public void onBalanceChanged() {
        for (AssetBalance assetBalance : BinanceApi.getInstance().getAccountBalance().getAccountBalanceCache().values()) {
            String assetname = assetBalance.getAsset();
            if (assetname.startsWith("LD"))
            {
                assetname = assetname.replace("LD","");
                AssetModel a = getAssetModel(assetname);
                a.setSavedValue(Double.parseDouble(assetBalance.getFree()));
            }
            else {
                AssetModel a = getAssetModel(assetBalance.getAsset());
                a.setAccountBalance(assetBalance);
            }
        }
    }

    private void getProfitsFromDb()
    {
        Log.d(TAG,"getProfitsFromDB");
        List<Profit> profitList = SingletonDataBase.appDatabase.profitDao().getAll();
        if (profitList != null)
        {
            for (Profit profit : profitList)
            {
                AssetModel assetModel = getAssetModel(profit.asset);
                assetModel.setProfit(profit.profit);
                assetModel.setTradescount(profit.tradescount);
            }
            Log.d(TAG,"loaded profits: " + profitList.size());
            //applyHasmapToLiveData();
        }
    }

    private void getAssetModelsFromDB()
    {
        Log.d(TAG,"getAssetsFromDB");
        List<AssetModel> assetModels = SingletonDataBase.appDatabase.assetModelDao().getAll();
        for (AssetModel assetModel: assetModels)
            assetModelHashMap.put(assetModel.assetName,assetModel);
        Log.d(TAG,"loaded assets:" + assetModels.size());
    }

    private void saveAssetModelsToDB()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SingletonDataBase.appDatabase.assetModelDao().insertAll(assetModelHashMap.values());
                }
                catch (IllegalStateException ex)
                {
                    ex.printStackTrace();
                }

                /*for (AssetModel assetModel: assetModelHashMap.values())
                    SingletonDataBase.appDatabase.assetModelDao().insert(assetModel);*/
            }
        }).start();
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
                }
            });
            BinanceApi.getInstance().getTicker().start();
        }
    }
}
