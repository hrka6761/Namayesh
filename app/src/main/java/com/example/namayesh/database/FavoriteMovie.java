package com.example.namayesh.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteMovie {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "movie_id")
    public String movie_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link_img")
    public String link_img;

    @ColumnInfo(name = "director")
    public String director;

    @ColumnInfo(name = "rate_imdb")
    public String rate_imdb;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "publish_date")
    public String publish_date;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "rank")
    public String rank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink_img() {
        return link_img;
    }

    public void setLink_img(String link_img) {
        this.link_img = link_img;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRate_imdb() {
        return rate_imdb;
    }

    public void setRate_imdb(String rate_imdb) {
        this.rate_imdb = rate_imdb;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
