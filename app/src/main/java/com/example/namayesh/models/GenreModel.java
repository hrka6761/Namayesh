package com.example.namayesh.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class GenreModel {

    private String id;
    private String genre_name;
    private String link_img;

    public GenreModel(String id, String genre_name, String link_img) {
        this.id = id;
        this.genre_name = genre_name;
        this.link_img = link_img;
    }

    public GenreModel() {
    }

    @BindingAdapter("SetGenreImage")
    public static void SetGenreImage(ImageView imageView , String link_img){
        Glide.with(imageView.getContext()).load(link_img).into(imageView);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public String getLink_img() {
        return link_img;
    }

    public void setLink_img(String link_img) {
        this.link_img = link_img;
    }
}
