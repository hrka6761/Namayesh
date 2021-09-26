package com.example.namayesh.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DetailsMovieModel implements Parcelable {

    private String id;
    private String movie_id;
    private String description;
    private String link_img;
    private String link_movie;
    private ArrayList<ArtistModel> artisList;
    private ArrayList<SerieEpizodModel> epizodList;


    public DetailsMovieModel(String id, String movie_id, String description, String link_img, String link_movie, ArrayList<ArtistModel> artistList,ArrayList<SerieEpizodModel> epizodtList) {
        this.id = id;
        this.movie_id = movie_id;
        this.description = description;
        this.link_img = link_img;
        this.link_movie = link_movie;
        this.artisList = artistList;
        this.epizodList = epizodtList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink_img() {
        return link_img;
    }

    public void setLink_img(String link_img) {
        this.link_img = link_img;
    }

    public String getLink_movie() {
        return link_movie;
    }

    public void setLink_movie(String link_movie) {
        this.link_movie = link_movie;
    }

    public ArrayList<ArtistModel> getArtistList() {
        return artisList;
    }

    public void setArtistList(ArrayList<ArtistModel> artistList) {
        this.artisList = artistList;
    }

    public ArrayList<SerieEpizodModel> getEpizodtList() {
        return epizodList;
    }

    public void setEpizodtList(ArrayList<SerieEpizodModel> epizodtList) {
        this.epizodList = epizodList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.movie_id);
        dest.writeString(this.description);
        dest.writeString(this.link_img);
        dest.writeString(this.link_movie);
        dest.writeList(this.artisList);
        dest.writeList(this.epizodList);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.movie_id = source.readString();
        this.description = source.readString();
        this.link_img = source.readString();
        this.link_movie = source.readString();
        this.artisList = new ArrayList<ArtistModel>();
        source.readList(this.artisList, ArtistModel.class.getClassLoader());
        this.epizodList = new ArrayList<SerieEpizodModel>();
        source.readList(this.epizodList, SerieEpizodModel.class.getClassLoader());
    }

    protected DetailsMovieModel(Parcel in) {
        this.id = in.readString();
        this.movie_id = in.readString();
        this.description = in.readString();
        this.link_img = in.readString();
        this.link_movie = in.readString();
        this.artisList = new ArrayList<ArtistModel>();
        in.readList(this.artisList, ArtistModel.class.getClassLoader());
        this.epizodList = new ArrayList<SerieEpizodModel>();
        in.readList(this.epizodList, SerieEpizodModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<DetailsMovieModel> CREATOR = new Parcelable.Creator<DetailsMovieModel>() {
        @Override
        public DetailsMovieModel createFromParcel(Parcel source) {
            return new DetailsMovieModel(source);
        }

        @Override
        public DetailsMovieModel[] newArray(int size) {
            return new DetailsMovieModel[size];
        }
    };
}
