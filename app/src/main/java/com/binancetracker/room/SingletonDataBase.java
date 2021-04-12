package com.binancetracker.room;

import android.content.Context;

import androidx.room.Room;

public class SingletonDataBase {

    public static BinanceDatabase binanceDatabase;
    public static AppDatabase appDatabase;

    public static void init(Context context)
    {
        binanceDatabase = Room.databaseBuilder(context,
                BinanceDatabase.class, "binanceDB").fallbackToDestructiveMigration().build();

        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, "appDB").fallbackToDestructiveMigration().build();
    }

    public static void close()
    {
        binanceDatabase.close();
        binanceDatabase = null;
    }
}
