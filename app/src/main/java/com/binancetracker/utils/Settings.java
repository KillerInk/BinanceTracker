package com.binancetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    SharedPreferences sharedPreferences;
    private static final String SECRETKEY = "secretkey";
    private static final String KEY = "key";

    private static Settings settings;

    public static Settings getInstance()
    {
        return settings;
    }

    public Settings(Context context)
    {
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

}
