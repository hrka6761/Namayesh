package com.example.namayesh.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteMovie.class}, version = 1)
public abstract class NamayeshDB extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();
    public static NamayeshDB instance;

    public static NamayeshDB getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, NamayeshDB.class, "namayesh")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
