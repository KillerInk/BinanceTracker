package com.binancetracker.ui.main;


import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.binance.api.client.domain.account.AssetBalance;
import com.binancetracker.BR;
import com.binancetracker.utils.ConvertingUtil;


public class AssetModel extends BaseObservable {

    private String assetName;
    private double freeValue;
    private double lockedValue;
    private double price = 0;
    private int priceColor = Color.WHITE;
    private double choosenAssetPrice = 0;
    private String choosenAsset;

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

    public void setPrice(double price) {
        if (price > this.price)
            priceColor = Color.GREEN;
        else if (price < this.price)
            priceColor = Color.RED;
        else
            priceColor = Color.WHITE;
        this.price = price;
        notifyPropertyChanged(BR.price);
        notifyPropertyChanged(BR.totalValuePrice);
        notifyPropertyChanged(BR.priceColor);
    }

    public void setChoosenAssetPrice(double choosenAssetPrice) {
        this.choosenAssetPrice = choosenAssetPrice;
        notifyPropertyChanged(BR.choosenAssetPrice);
        notifyPropertyChanged(BR.totalValueChoosenPrice);
    }

    @Bindable
    public double getChoosenAssetPrice() {
        if (price > 0)
            return ConvertingUtil.trimDoubleToPlaces(price/choosenAssetPrice,2);
        return 0;
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    @Bindable
    public double getTotalValuePrice(){ return ConvertingUtil.trimDoubleToPlaces((freeValue + lockedValue) * price,2); }

    public void setTotalValuePrice(double val){}

    @Bindable
    public double getTotalValueChoosenPrice(){
        if (price > 0)
            return ConvertingUtil.trimDoubleToPlaces(((freeValue + lockedValue) * (price/choosenAssetPrice)),2);
        else  return 0;
    }

    public void setTotalValueChoosenPrice(double val){}

    @Bindable
    public String getChoosenAsset() {
        return choosenAsset;
    }

    public void setChoosenAsset(String choosenAsset) {
        this.choosenAsset = choosenAsset;
        notifyPropertyChanged(BR.choosenAsset);
    }

    @Bindable
    public int getPriceColor() {
        return priceColor;
    }

    public void setPriceColor(int priceColor) {
        this.priceColor = priceColor;
    }
}