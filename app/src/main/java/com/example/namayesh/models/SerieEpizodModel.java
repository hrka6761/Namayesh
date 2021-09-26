package com.example.namayesh.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class SerieEpizodModel implements Parcelable {

    public String id;
    public String movie_id;
    public String epizod_link;
    public String part;
    public String serie_link;

    public SerieEpizodModel(String id, String movie_id, String epizod_link, String part, String serie_link) {
        this.id = id;
        this.movie_id = movie_id;
        this.epizod_link = epizod_link;
        this.part = part;
        this.serie_link = serie_link;
    }


    @BindingAdapter("SetEpizodImage")
    public static void SetEpizodImage(ImageView imageView, String epizod_link) {
        Glide.with(imageView.getContext()).load(epizod_link).into(imageView);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpizod_link() {
        return epizod_link;
    }

    public void setEpizod_link(String epizod_link) {
        this.epizod_link = epizod_link;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getSerie_link() {
        return serie_link;
    }

    public void setSerie_link(String serie_link) {
        this.serie_link = serie_link;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.movie_id);
        dest.writeString(this.epizod_link);
        dest.writeString(this.part);
        dest.writeString(this.serie_link);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.movie_id = source.readString();
        this.epizod_link = source.readString();
        this.part = source.readString();
        this.serie_link = source.readString();
    }

    protected SerieEpizodModel(Parcel in) {
        this.id = in.readString();
        this.movie_id = in.readString();
        this.epizod_link = in.readString();
        this.part = in.readString();
        this.serie_link = in.readString();
    }

    public static final Parcelable.Creator<SerieEpizodModel> CREATOR = new Parcelable.Creator<SerieEpizodModel>() {
        @Override
        public SerieEpizodModel createFromParcel(Parcel source) {
            return new SerieEpizodModel(source);
        }

        @Override
        public SerieEpizodModel[] newArray(int size) {
            return new SerieEpizodModel[size];
        }
    };
}
