package com.binancetracker.ui.main;

import androidx.lifecycle.ViewModel;


import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;

import java.util.Map;
import java.util.TreeMap;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private BinanceApiClientFactory clientFactory;

    /**
     * Key is the symbol, and the value is the balance of that symbol on the account.
     */
    private Map<String, AssetBalance> accountBalanceCache;

    /**
     * Listen key used to interact with the user data streaming API.
     */
    private String listenKey;

    public void onResume(String apiKey, String secret) {
        this.clientFactory = BinanceApiClientFactory.newInstance(apiKey, secret);
        this.listenKey = initializeAssetBalanceCacheAndStreamSession();
        //startAccountBalanceEventStreaming(listenKey);
    }

    /**
     * Initializes the asset balance cache by using the REST API and starts a new user data streaming session.
     *
     * @return a listenKey that can be used with the user data streaming API.
     */
    private String initializeAssetBalanceCacheAndStreamSession() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        Account account = client.getAccount();

        this.accountBalanceCache = new TreeMap<>();
        for (AssetBalance assetBalance : account.getBalances()) {
            accountBalanceCache.put(assetBalance.getAsset(), assetBalance);
        }

        return client.startUserDataStream();
    }

    public void getPortofolio()
    {

    }
}