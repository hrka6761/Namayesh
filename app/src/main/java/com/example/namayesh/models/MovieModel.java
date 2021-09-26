package com.example.namayesh.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class MovieModel implements Parcelable {
    private String id;
    private String name;
    private String link_img;
    private String director;
    private String rate_imdb;
    private String time;
    private String publish_date;
    private String category;
    private String rank;

    public MovieModel(String id, String name, String link_img, String director, String rate_imdb, String time, String publish_date, String category, String rank) {
        this.id = id;
        this.name = name;
        this.link_img = link_img;
        this.director = director;
        this.rate_imdb = rate_imdb;
        this.time = time;
        this.publish_date = publish_date;
        this.category = category;
        this.rank = rank;
    }

    @BindingAdapter("SetSlideImage")
    public static void SetSlideImage(ImageView imageView , String link_img){
        Glide.with(imageView.getContext()).load(link_img).into(imageView);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.link_img);
        dest.writeString(this.director);
        dest.writeString(this.rate_imdb);
        dest.writeString(this.time);
        dest.writeString(this.publish_date);
        dest.writeString(this.category);
        dest.writeString(this.rank);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.link_img = source.readString();
        this.director = source.readString();
        this.rate_imdb = source.readString();
        this.time = source.readString();
        this.publish_date = source.readString();
        this.category = source.readString();
        this.rank = source.readString();
    }

    protected MovieModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.link_img = in.readString();
        this.director = in.readString();
        this.rate_imdb = in.readString();
        this.time = in.readString();
        this.publish_date = in.readString();
        this.category = in.readString();
        this.rank = in.readString();
    }

    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}
