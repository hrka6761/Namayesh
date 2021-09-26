package com.example.namayesh.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    List<FavoriteMovie> getAllFavorite();

    @Query("SELECT * FROM favorite WHERE movie_id=:movie_id")
    FavoriteMovie getFavoriteMovie(String movie_id);

    @Insert
    void insertFavorite(FavoriteMovie... favoriteMovieModel);

    @Delete
    void deletFavorite(FavoriteMovie favoriteMovieModel);

    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE movie_id=:movie_id)")
    int isExistsFavorite(int movie_id);

}
