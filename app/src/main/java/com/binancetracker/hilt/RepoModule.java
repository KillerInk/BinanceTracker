package com.binancetracker.hilt;

import android.content.Context;

import com.binancetracker.repo.api.BinanceApi;
import com.binancetracker.repo.AssetRepo;
import com.binancetracker.repo.room.SingletonDataBase;
import com.binancetracker.utils.Settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepoModule
{

    @Provides
    @Singleton
    public static AssetRepo assetRepo( BinanceApi binanceApi,  Settings settings,  SingletonDataBase singletonDataBase)
    {
        return new AssetRepo(binanceApi,settings,singletonDataBase);
    }

    @Provides
    @Singleton
    public static BinanceApi binanceApi( Settings settings, SingletonDataBase singletonDataBase)
    {
        return new BinanceApi(settings,singletonDataBase);
    }


    @Provides
    @Singleton
    public static Settings settings(@ApplicationContext Context context)
    {
        return new Settings(context);
    }


    @Provides
    @Singleton
    public static SingletonDataBase singletonDataBase(@ApplicationContext Context context)
    {
        return new SingletonDataBase(context);
    }
}
