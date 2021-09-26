package com.example.namayesh.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ArtistModel implements Parcelable {

    private String id;
    private String movie_id;
    private String artist_name;
    private String artist_img;
    private String artist_role;

    public ArtistModel(String id, String movie_id, String artist_name, String artist_img, String artist_role) {
        this.id = id;
        this.movie_id = movie_id;
        this.artist_name = artist_name;
        this.artist_img = artist_img;
        this.artist_role = artist_role;
    }

    @BindingAdapter("SetArtistImage")
    public static void SetArtistImage(ImageView imageView , String artist_img){
        Glide.with(imageView.getContext()).load(artist_img).into(imageView);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getArtist_img() {
        return artist_img;
    }

    public void setArtist_img(String artist_img) {
        this.artist_img = artist_img;
    }

    public String getArtist_role() {
        return artist_role;
    }

    public void setArtist_role(String artist_role) {
        this.artist_role = artist_role;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.movie_id);
        dest.writeString(this.artist_name);
        dest.writeString(this.artist_img);
        dest.writeString(this.artist_role);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.movie_id = source.readString();
        this.artist_name = source.readString();
        this.artist_img = source.readString();
        this.artist_role = source.readString();
    }

    protected ArtistModel(Parcel in) {
        this.id = in.readString();
        this.movie_id = in.readString();
        this.artist_name = in.readString();
        this.artist_img = in.readString();
        this.artist_role = in.readString();
    }

    public static final Parcelable.Creator<ArtistModel> CREATOR = new Parcelable.Creator<ArtistModel>() {
        @Override
        public ArtistModel createFromParcel(Parcel source) {
            return new ArtistModel(source);
        }

        @Override
        public ArtistModel[] newArray(int size) {
            return new ArtistModel[size];
        }
    };
}
