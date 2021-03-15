package com.binancetracker.api;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static com.binance.api.client.domain.event.UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_UPDATE;

public class AccountBalance {

    public interface AccountBalanceEvent
    {
        void onBalanceChanged();
    }

    private BinanceApiClientFactory clientFactory;

    /**
     * Key is the symbol, and the value is the balance of that symbol on the account.
     */
    private Map<String, AssetBalance> accountBalanceCache;

    /**
     * Listen key used to interact with the user data streaming API.
     */
    private String listenKey;

    private AccountBalanceEvent accountBalanceEventListner;

    private Closeable userdataStream;

    public AccountBalance(BinanceApiClientFactory clientFactory)
    {
        this.clientFactory = clientFactory;
        this.accountBalanceCache = new TreeMap<>();
    }

    public void setAccountBalanceEventListner(AccountBalanceEvent accountBalanceEventListner)
    {
        this.accountBalanceEventListner = accountBalanceEventListner;
    }

    public void startListenToAssetBalance() {
        this.listenKey = initializeAssetBalanceCacheAndStreamSession();
        startAccountBalanceEventStreaming(listenKey);
    }

    public void stopListenToAssetBalance()
    {
        if (userdataStream != null)
            try {
                userdataStream.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        userdataStream = null;
    }

    public Map<String, AssetBalance> getAccountBalanceCache() {
        return accountBalanceCache;
    }

    /**
     * Initializes the asset balance cache by using the REST API and starts a new user data streaming session.
     *
     * @return a listenKey that can be used with the user data streaming API.
     */
    private String initializeAssetBalanceCacheAndStreamSession() {
        BinanceApiRestClient client = clientFactory.newRestClient();
        Account account = client.getAccount();
        accountBalanceCache.clear();
        for (AssetBalance assetBalance : account.getBalances()) {
            if (haveBalance(assetBalance))
                accountBalanceCache.put(assetBalance.getAsset(), assetBalance);
        }
        if (accountBalanceEventListner != null)
            accountBalanceEventListner.onBalanceChanged();

        return client.startUserDataStream();
    }

    private boolean haveBalance(AssetBalance assetBalance)
    {
        double free = Double.parseDouble(assetBalance.getFree());
        double locked = Double.parseDouble(assetBalance.getLocked());
        if (free + locked > 0)
            return true;
        return false;
    }

    /**
     * Begins streaming of agg trades events.
     */
    private void startAccountBalanceEventStreaming(String listenKey) {
        BinanceApiWebSocketClient client = clientFactory.newWebSocketClient();

        userdataStream = client.onUserDataUpdateEvent(listenKey, response -> {
            if (response.getEventType() == ACCOUNT_UPDATE) {
                // Override cached asset balances
                for (AssetBalance assetBalance : response.getAccountUpdateEvent().getBalances()) {
                    accountBalanceCache.put(assetBalance.getAsset(), assetBalance);
                }
                if (accountBalanceEventListner != null)
                    accountBalanceEventListner.onBalanceChanged();
                //System.out.println(accountBalanceCache);
            }
        });
    }
}
