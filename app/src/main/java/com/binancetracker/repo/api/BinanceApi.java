package com.binancetracker.repo.api;


import com.binance.api.client.api.sync.BinanceApiSpotRestClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.factory.BinanceAbstractFactory;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;

import com.binancetracker.repo.api.runnable.DownloadFuturesTransactionHistory;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.Settings;

import javax.inject.Inject;

public class BinanceApi {

    private BinanceSpotApiClientFactory spotClientFactory;
    private AccountBalance accountBalance;
    private Ticker ticker;
    private DownloadTradeHistory downloadTradeHistory;
    private DownloadDepositHistory downloadDespositHistory;
    private DownloadWithdrawHistory downloadWithdrawHistory;
    private DownloadCandleStickHistory downloadCandleStickHistory;
    private DownloadFuturesHistory downloadFuturesHistory;
    private DownloadSwapHistory downloadSwapHistory;
    private Settings settings;
    private SingletonDataBase singletonDataBase;

    @Inject
    public BinanceApi(Settings settings, SingletonDataBase singletonDataBase)
    {
        this.settings = settings;
        this.singletonDataBase = singletonDataBase;
    }

    public void setKeys(String key, String secretKey) {
        spotClientFactory = BinanceAbstractFactory.createSpotFactory(key, secretKey);
        accountBalance = new AccountBalance(spotClientFactory);
        ticker = new Ticker(spotClientFactory);
        downloadTradeHistory = new DownloadTradeHistory(spotClientFactory,singletonDataBase);
        downloadDespositHistory = new DownloadDepositHistory(spotClientFactory,singletonDataBase);
        downloadWithdrawHistory = new DownloadWithdrawHistory(spotClientFactory,singletonDataBase);
        downloadCandleStickHistory = new DownloadCandleStickHistory(spotClientFactory,singletonDataBase,settings);
        downloadFuturesHistory = new DownloadFuturesHistory(spotClientFactory,singletonDataBase);
        downloadSwapHistory = new DownloadSwapHistory(BinanceAbstractFactory.createSwapFactory(key,secretKey),singletonDataBase);
    }

    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public DownloadTradeHistory getDownloadTradeHistory() {
        return downloadTradeHistory;
    }

    public DownloadDepositHistory getDownloadDespositHistory() {
        return downloadDespositHistory;
    }

    public DownloadWithdrawHistory getDownloadWithdrawHistory() {
        return downloadWithdrawHistory;
    }


    public DownloadCandleStickHistory getDownloadCandleStickHistory() {
        return downloadCandleStickHistory;
    }

    public DownloadFuturesHistory getDownloadFuturesHistory() {
        return downloadFuturesHistory;
    }

    public DownloadSwapHistory getDownloadSwapHistory() {
        return downloadSwapHistory;
    }

    private Account account = null;
    public String testKey()
    {
        Object ob = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {

                BinanceApiSpotRestClient client = spotClientFactory.newRestClient();

                // Get account balances
                synchronized (ob) {
                    account = client.getAccount(60_000L, System.currentTimeMillis());
                    ob.notify();
                }
            }
        }).start();
        synchronized (ob)
        {
            try {
                ob.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return account.toString();
    }
}
