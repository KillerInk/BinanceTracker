package com.binancetracker.room;

import android.content.Context;

import androidx.room.Room;

public class SingletonDataBase {

    public static AppDatabase appDatabase;

    public static void init(Context context)
    {
        appDatabase = Room.databaseBuilder(context,
                AppDatabase.class, "markets").fallbackToDestructiveMigration().build();
    }

    public static void close()
    {
        appDatabase.close();
        appDatabase = null;
    }
}
