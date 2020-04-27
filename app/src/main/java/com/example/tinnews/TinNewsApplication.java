package com.example.tinnews;

import android.app.Application;

import androidx.room.Room;

import com.ashokvarma.gander.Gander;
import com.ashokvarma.gander.imdb.GanderIMDB;
import com.example.tinnews.database.AppDatabase;
import com.facebook.stetho.Stetho;

public class TinNewsApplication extends Application {

    private  static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        Gander.setGanderStorage(GanderIMDB.getInstance());
        Stetho.initializeWithDefaults(this);
            database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tin_db").build();
        }
        public static AppDatabase getDatabase() {
            return database;
    }
}
