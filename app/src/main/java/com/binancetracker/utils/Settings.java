package com.binancetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.binancetracker.R;

public class Settings {

    private static final String SECRETKEY = "secretkey";
    private static final String KEY = "key";
    private static final String defaultAsset = "defaultAsset";

    private static Settings settings;

    public static Settings getInstance()
    {
        return settings;
    }


    private SharedPreferences sharedPreferences;
    private Context context;

    public Settings(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("keys",Context.MODE_PRIVATE);
        settings = this;
    }

    public String getSECRETKEY() {
        return sharedPreferences.getString(SECRETKEY,"");
    }

    public void setSecretKey(String key)
    {
        sharedPreferences.edit().putString(SECRETKEY,key).apply();
    }

    public String getKEY() {
        return sharedPreferences.getString(KEY,"");
    }

    public void setKey(String key)
    {
        sharedPreferences.edit().putString(KEY,key).apply();
    }

    public String getDefaultAsset() {
        return sharedPreferences.getString(defaultAsset,context.getString(R.string.usdt));
    }

    public void setDefaultAsset(String asset)
    {
        sharedPreferences.edit().putString(defaultAsset,asset).apply();
    }


}
