package com.binancetracker.repo.api.runnable;

import com.binance.api.client.factory.BinanceFactory;
import com.binance.api.client.factory.BinanceSpotApiClientFactory;
import com.binancetracker.repo.room.SingletonDataBase;

public abstract class ClientFactoryRunner<T extends BinanceFactory> implements Runnable {

    public interface MessageEvent
    {
        void onSyncStart(int max);
        void onSyncUpdate(int currentmarket,String msg);
        void onSyncEnd();
    }

    protected T clientFactory;
    protected SingletonDataBase singletonDataBase;
    protected MessageEvent messageEventListner;

    public ClientFactoryRunner(T clientFactory,SingletonDataBase singletonDataBase)
    {
        this.clientFactory = clientFactory;
        this.singletonDataBase = singletonDataBase;
    }

    public void setMessageEventListner(MessageEvent messageEventListner) {
        this.messageEventListner = messageEventListner;
    }

    protected void fireOnSyncStart(int max)
    {
        if (messageEventListner != null)
            messageEventListner.onSyncStart(max);
    }

    protected void fireOnSyncUpdate(int current, String msg)
    {
        if (messageEventListner != null)
            messageEventListner.onSyncUpdate(current,msg);
    }

    protected void fireOnSyncEnd()
    {
        if (messageEventListner != null)
            messageEventListner.onSyncEnd();
    }
}
