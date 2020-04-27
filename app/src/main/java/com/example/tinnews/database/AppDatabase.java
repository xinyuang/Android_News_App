package com.example.tinnews.database;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tinnews.model.Article;

import java.util.List;

@Database(entities = {Article.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomDao dao();
}
