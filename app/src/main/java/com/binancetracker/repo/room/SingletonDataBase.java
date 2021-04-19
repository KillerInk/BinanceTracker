package com.binancetracker.repo.room;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class SingletonDataBase {

    public final BinanceDatabase binanceDatabase;
    public final AppDatabase appDatabase;

    @Inject
    public SingletonDataBase(@ApplicationContext Context context)
    {
        binanceDatabase = Room.databaseBuilder(context,
                BinanceDatabase.class, "binanceDB").fallbackToDestructiveMigration().build();

        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, "appDB").fallbackToDestructiveMigration().build();
    }

    public void close()
    {
        binanceDatabase.close();
        appDatabase.close();
    }
}
