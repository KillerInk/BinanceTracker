package com.binancetracker.api;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.Account;

public class BinanceApi {



    private BinanceApiClientFactory clientFactory;
    private AccountBalance accountBalance;
    private Ticker ticker;
    private DownloadTradeHistory downloadTradeHistory;



    private static BinanceApi binanceApi = new BinanceApi();

    public static BinanceApi getInstance() {
        return binanceApi;
    }

    public void setKeys(String key, String secretKey) {
        clientFactory = BinanceApiClientFactory.newInstance(key, secretKey);
        accountBalance = new AccountBalance(clientFactory);
        ticker = new Ticker(clientFactory);
        downloadTradeHistory = new DownloadTradeHistory(clientFactory);
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

    private Account account = null;
    public String testKey()
    {
        Object ob = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {

                BinanceApiRestClient client = clientFactory.newRestClient();

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
