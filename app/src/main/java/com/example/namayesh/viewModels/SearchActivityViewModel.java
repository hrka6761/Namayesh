package com.example.namayesh.viewModels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.namayesh.models.MovieModel;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.Disposable;


public class SearchActivityViewModel extends ViewModel {

    MutableLiveData<ArrayList<MovieModel>> movieListMutableLiveData;



    public MutableLiveData<ArrayList<MovieModel>> getMovieListMutableLiveData() {

        if (movieListMutableLiveData == null) {
            movieListMutableLiveData = new MutableLiveData<>();
        }

        return movieListMutableLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
