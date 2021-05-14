package com.binancetracker.repo;

import android.util.Log;

import com.binance.api.client.domain.account.AssetBalance;
import com.binancetracker.repo.api.AccountBalance;
import com.binancetracker.repo.api.Ticker;
import com.binancetracker.repo.api.runnable.account.Download30DaysDepositHistoryRunner;
import com.binancetracker.repo.api.runnable.account.Download30DaysWithdrawHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadLastTradeHistoryRunner;
import com.binancetracker.repo.api.runnable.market.DownloadLatestDayHistoryForAllPairsRunner;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.repo.room.entity.Profit;
import com.binancetracker.repo.thread.RestExecuter;
import com.binancetracker.ui.main.AssetModel;
import com.binancetracker.utils.CalcProfits;
import com.binancetracker.utils.MarketPair;
import com.binancetracker.utils.MyTime;
import com.binancetracker.utils.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class  AssetRepo implements AccountBalance.AccountBalanceEvent {

    public interface AssetEvent
    {
        void onAssetChanged();
    }


    private final String TAG = AssetRepo.class.getSimpleName();
    private HashMap<String,AssetModel> assetModelHashMap;
    private final String base = "USDT";
    private String choosenAsset;
    private AssetEvent assetEventListner;
    private Settings settings;
    private SingletonDataBase singletonDataBase;
    private AccountBalance accountBalance;
    private DownloadLastTradeHistoryRunner downloadLastTradeHistoryRunner;
    private Download30DaysDepositHistoryRunner download30DaysDepositHistoryRunner;
    private Download30DaysWithdrawHistoryRunner download30DaysWithdrawHistoryRunner;
    private DownloadLatestDayHistoryForAllPairsRunner downloadLatestDayHistoryForAllPairsRunner;
    private Ticker ticker;

    @Inject
    public AssetRepo(
                     Settings settings,
                     SingletonDataBase singletonDataBase,
                     AccountBalance accountBalance,
                     DownloadLastTradeHistoryRunner downloadLastTradeHistoryRunner,
                     Download30DaysDepositHistoryRunner download30DaysDepositHistoryRunner,
                     Download30DaysWithdrawHistoryRunner download30DaysWithdrawHistoryRunner,
                     DownloadLatestDayHistoryForAllPairsRunner downloadLatestDayHistoryForAllPairsRunner,
                     Ticker ticker)
    {
        this.settings = settings;
        this.singletonDataBase = singletonDataBase;
        this.accountBalance = accountBalance;
        this.downloadLastTradeHistoryRunner = downloadLastTradeHistoryRunner;
        this.download30DaysDepositHistoryRunner = download30DaysDepositHistoryRunner;
        this.download30DaysWithdrawHistoryRunner = download30DaysWithdrawHistoryRunner;
        this.downloadLatestDayHistoryForAllPairsRunner = downloadLatestDayHistoryForAllPairsRunner;
        this.ticker = ticker;
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
        Log.v(TAG, "onResume");
        if (settings.getSECRETKEY().equals("") || settings.getKEY().equals(""))
            return;
        choosenAsset = settings.getDefaultAsset();

        accountBalance.setAccountBalanceEventListner(this::onBalanceChanged);

        RestExecuter.addTask(onResumeRunner);
    }

    private Runnable onResumeRunner = new Runnable() {
        @Override
        public void run() {
            getAssetModelsFromDB();
            fireAssetChangedEvent();
            getProfitsFromDb();
            fireAssetChangedEvent();
            accountBalance.startListenToAssetBalance();
            fireAssetChangedEvent();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updatedPrices();
                }
            }).start();
            MyTime lastSync = new MyTime(settings.getLastSync()).setMinutes(5);
            if (lastSync.getTime() < System.currentTimeMillis()) {
                updateHistory();
            }
        }
    };

    public void updateHistory()
    {
        RestExecuter.addTask(updateHistoryRunner);
        settings.setLastSync(System.currentTimeMillis());
    }

    private Runnable updateHistoryRunner = new Runnable() {
        @Override
        public void run() {
            downloadLastTradeHistoryRunner.run();
            download30DaysDepositHistoryRunner.run();
            download30DaysWithdrawHistoryRunner.run();
            downloadLatestDayHistoryForAllPairsRunner.run();
            new CalcProfits().calcProfits(singletonDataBase);
            getProfitsFromDb();
        }
    };

    public void onPause()
    {
        accountBalance.setAccountBalanceEventListner(null);
        accountBalance.stopListenToAssetBalance();
        ticker.setPriceChangedEvent(null);
        ticker.stop();
        saveAssetModelsToDB();
    }

    private void fireAssetChangedEvent()
    {
        if (assetEventListner != null)
            assetEventListner.onAssetChanged();
    }

    @Override
    public void onBalanceChanged() {

        List<String> savingassets = new ArrayList<>();
        for (AssetBalance assetBalance : accountBalance.getAccountBalanceCache().values()) {
            String assetname = assetBalance.getAsset();
            if (assetname.startsWith("LD"))
            {
                assetname = assetname.replace("LD","");
                savingassets.add(assetname);
                AssetModel a = getAssetModel(assetname);
                a.setSavedValue(Double.parseDouble(assetBalance.getFree()));
            }
            else {
                if (!savingassets.contains(assetname)) {
                    AssetModel a = getAssetModel(assetname);
                    a.setSavedValue(0);
                }
                AssetModel a = getAssetModel(assetBalance.getAsset());
                a.setAccountBalance(assetBalance);
            }
        }
    }

    private void getProfitsFromDb()
    {
        Log.d(TAG,"getProfitsFromDB");
        List<Profit> profitList = singletonDataBase.appDatabase.profitDao().getAll();
        if (profitList != null)
        {
            for (Profit profit : profitList)
            {
                AssetModel assetModel = getAssetModel(profit.asset);
                assetModel.setProfit(profit.profit);
                assetModel.setTradescount(profit.tradescount);
                assetModel.setDeposits(profit.deposits);
                assetModel.setWithdraws(profit.withdraws);
            }
            Log.d(TAG,"loaded profits: " + profitList.size());
            //applyHasmapToLiveData();
        }
    }

    private void getAssetModelsFromDB()
    {
        Log.d(TAG,"getAssetsFromDB");
        List<AssetModel> assetModels = singletonDataBase.appDatabase.assetModelDao().getAll();
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
                    singletonDataBase.appDatabase.assetModelDao().insertAll(assetModelHashMap.values());
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
            assetModel.setChoosenAsset(settings.getDefaultAsset());
            assetModelHashMap.put(symbol,assetModel);
            singletonDataBase.appDatabase.assetModelDao().insert(assetModel);
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
            ticker.setMarketsToListen(markets);
            ticker.setPriceChangedEvent(new Ticker.PriceChangedEvent() {
                @Override
                public void onPriceChanged(String symbol,final double price,String priceChang) {
                    //Log.d(TAG,"onPriceChanged:" + symbol + " " + price);
                    AssetModel assetModel = getAssetModel(new MarketPair(symbol).getQuoteAsset());
                    if (symbol.contains(assetModel.getAssetName())) {
                        assetModel.setPrice(price);
                        assetModel.setChanged24hpercentage(priceChang);
                    }
                    //set choosen asset price like eur
                    if (symbol.equals(choosenAsset+base) && assetModel.getAssetName().equals(choosenAsset)) {
                        for (AssetModel a : assetModelHashMap.values())
                            a.setChoosenAssetPrice(price);
                        //
                    }
                }
            });
            ticker.start();
        }
    }
}
