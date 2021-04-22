package com.binancetracker.repo.api;

import com.binancetracker.repo.api.runnable.ClientFactoryRunner;

public interface MessageEventListner {
    void setMessageEventListner(ClientFactoryRunner.MessageEvent messageEventListner);
}
