package com.binancetracker.ui.main;


import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.binance.api.client.domain.account.AssetBalance;
import com.binancetracker.BR;
import com.binancetracker.utils.ConvertingUtil;

@Entity
public class AssetModel extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "asset")
    public String assetName;
    @ColumnInfo(name = "freeValue")
    public double freeValue;
    @ColumnInfo(name = "lockedValue")
    public double lockedValue;
    @ColumnInfo(name = "savedValue")
    public double savedValue;
    @ColumnInfo(name = "price")
    public double price = 0;
    private int priceColor = Color.WHITE;
    @ColumnInfo(name = "choosenAssetPrice")
    public double choosenAssetPrice = 0;
    @ColumnInfo(name = "choosenAsset")
    public String choosenAsset;

    private double profit;
    private long tradescount;
    private double deposits;
    private double withdraws;

    public AssetModel()
    {}

    public AssetModel(AssetBalance accountBalance)
    {
        setAssetName(accountBalance.getAsset());
        setFreeValue(Double.parseDouble(accountBalance.getFree()));
        setLockedValue(Double.parseDouble(accountBalance.getLocked()));
        notifyAssetChanged();
    }

    public void setAccountBalance(AssetBalance accountBalance)
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
        return trim(freeValue);
    }

    public void setFreeValue(double freeValue) {
        this.freeValue = freeValue;
    }

    @Bindable
    public double getLockedValue() {
        return trim(lockedValue);
    }

    public void setLockedValue(double lockedValue) {
        this.lockedValue = lockedValue;
    }

    @Bindable
    public double getTotalValue(){ return trim(freeValue + lockedValue + savedValue); }

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
        notifyPropertyChanged(BR.freeValueUsdtPrice);
        notifyPropertyChanged(BR.lockedValueUsdtPrice);
        notifyPropertyChanged(BR.savingValueUsdtPrice);
        notifyPropertyChanged(BR.totalValueChoosenAssetPrice);
        notifyPropertyChanged(BR.lockedValueChoosenAssetPrice);
        notifyPropertyChanged(BR.savingValueChoosenAssetPrice);
        notifyPropertyChanged(BR.freeValueChoosenAssetPrice);
    }

    @Bindable
    public double getChoosenAssetPrice() {
        return trim(price/choosenAssetPrice);
    }

    private double trim(double val)
    {
        if (val > 0 && val > 10)
            return ConvertingUtil.trimDoubleToPlaces(val,2);
        else if (val > 0 && val<= 10)
            return ConvertingUtil.trimDoubleToPlaces(val,8);
        return 0;
    }

    @Bindable
    public double getPrice() {
        return price;
    }

    @Bindable
    public double getTotalValuePrice(){ return trim((freeValue + lockedValue + savedValue) * price); }

    public void setTotalValuePrice(double val){}

    public void setTotalValueChoosenAssetPrice(double val){}
    @Bindable
    public double getTotalValueChoosenAssetPrice(){
        if (price > 0)
            return trim(((freeValue + lockedValue + savedValue) * (price/choosenAssetPrice)));
        else  return 0;
    }

    public void setFreeValueChoosenAssetPrice(double val){}

    @Bindable
    public double getFreeValueChoosenAssetPrice(){
        if (price > 0)
            return trim(((freeValue) * (price/choosenAssetPrice)));
        else  return 0;
    }

    public void setLockedValueChoosenAssetPrice(double val){}

    @Bindable
    public double getLockedValueChoosenAssetPrice(){
        if (price > 0)
            return trim(((lockedValue) * (price/choosenAssetPrice)));
        else  return 0;
    }

    public void setSavingValueChoosenAssetPrice(double val){}

    @Bindable
    public double getSavingValueChoosenAssetPrice(){
        if (price > 0)
            return trim(((savedValue) * (price/choosenAssetPrice)));
        else  return 0;
    }

    public void setFreeValueUsdtPrice(double val){}

    @Bindable
    public double getFreeValueUsdtPrice(){
        if (price > 0)
            return trim(((freeValue) * (price)));
        else  return 0;
    }

    public void setLockedValueUsdtPrice(double val){}

    @Bindable
    public double getLockedValueUsdtPrice(){
        if (price > 0)
            return trim(((lockedValue) * (price)));
        else  return 0;
    }

    public void setSavingValueUsdtPrice(double val){}

    @Bindable
    public double getSavingValueUsdtPrice(){
        if (price > 0)
            return trim(((savedValue) * (price)));
        else  return 0;
    }



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

    @Bindable
    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
        notifyPropertyChanged(BR.profit);
    }

    @Bindable
    public long getTradescount() {
        return tradescount;
    }

    public void setTradescount(long tradescount) {
        this.tradescount = tradescount;
        notifyPropertyChanged(BR.tradescount);
    }

    @Bindable
    public double getSavedValue() {
        return savedValue;
    }

    public void setSavedValue(double savedValue) {
        this.savedValue = savedValue;
        notifyPropertyChanged(BR.savedValue);
        notifyPropertyChanged(BR.totalValue);
    }

    @Bindable
    public double getDeposits() {
        return deposits;
    }

    public void setDeposits(double deposits) {
        this.deposits = deposits;
        notifyPropertyChanged(BR.deposits);
    }

    @Bindable
    public double getWithdraws() {
        return withdraws;
    }

    public void setWithdraws(double withdraws) {
        this.withdraws = withdraws;
        notifyPropertyChanged(BR.withdraws);
    }
}
