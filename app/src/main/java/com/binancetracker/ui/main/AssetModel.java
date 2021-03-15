package com.binancetracker.ui.main;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.binance.api.client.domain.account.AssetBalance;
import com.binancetracker.api.AccountBalance;

public class AssetModel extends BaseObservable {

    private String assetName;
    private double freeValue;
    private double lockedValue;

    public AssetModel()
    {}

    public AssetModel(AssetBalance accountBalance)
    {
        setAssetName(accountBalance.getAsset());
        setFreeValue(Double.parseDouble(accountBalance.getFree()));
        setLockedValue(Double.parseDouble(accountBalance.getLocked()));
        notifyAssetChanged();
    }

    @Bindable
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    @Bindable
    public double getFreeValue() {
        return freeValue;
    }

    public void setFreeValue(double freeValue) {
        this.freeValue = freeValue;
    }

    @Bindable
    public double getLockedValue() {
        return lockedValue;
    }

    public void setLockedValue(double lockedValue) {
        this.lockedValue = lockedValue;
    }

    @Bindable
    public double getTotalValue(){ return freeValue + lockedValue; }

    public void setTotalValue(double val){}

    public void notifyAssetChanged()
    {
        notifyChange();
    }
}
