package com.binancetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.binancetracker.R;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class Settings {

    private static final String SECRETKEY = "secretkey";
    private static final String KEY = "key";
    private static final String defaultAsset = "defaultAsset";
    private static final String lastSync = "lastSync";


    private SharedPreferences sharedPreferences;
    private Context context;

    @Inject
    public Settings(@ApplicationContext Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("keys",Context.MODE_PRIVATE);
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

    public long getLastSync()
    {
        return sharedPreferences.getLong(lastSync,0);
    }

    public void setLastSync(long time)
    {
        sharedPreferences.edit().putLong(lastSync,time);
    }


}
