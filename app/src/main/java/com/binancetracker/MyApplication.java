package com.binancetracker;

import android.app.Application;
import android.content.Context;

import java.io.File;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        context = null;
        super.onTerminate();
    }

    public static String getStringFromRes(int id)
    {
        return context.getResources().getString(id);
    }

    public static String[] getStringArrayFromRes(int id)
    {
        return context.getResources().getStringArray(id);
    }

    public static File getAppCacheDir()
    {
        return context.getCacheDir();
    }
}
