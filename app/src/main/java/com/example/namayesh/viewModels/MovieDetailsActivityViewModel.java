package com.example.namayesh.viewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.namayesh.models.DetailsMovieModel;
import com.example.namayesh.models.MovieLinkModel;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailsActivityViewModel extends AndroidViewModel {

    MutableLiveData<DetailsMovieModel> detailsMovieMutableLiveData;
    MutableLiveData<ArrayList<MovieLinkModel>> movieLinksMutableLiveData;

    ApiInterface request = ApiClient.getApiClient().create(ApiInterface.class);

    Disposable movieDetailDisposable;
    Disposable movieLinkDisposable;

    String selectedMovieDetail;

    public MovieDetailsActivityViewModel(@NonNull @NotNull Application application, String selectedMovieDetail) {
        super(application);
        this.selectedMovieDetail = selectedMovieDetail;
    }

    public MutableLiveData<DetailsMovieModel> getDetailsMovie() {

        if (detailsMovieMutableLiveData == null) {
            detailsMovieMutableLiveData = new MutableLiveData<>();

            request.receiveMovieDetail(selectedMovieDetail)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new Observer<DetailsMovieModel>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            movieDetailDisposable = d;
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull DetailsMovieModel detailsMovieModel) {
                            detailsMovieMutableLiveData.postValue(detailsMovieModel);
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            movieDetailDisposable.dispose();

                        }

                        @Override
                        public void onComplete() {
                            movieDetailDisposable.dispose();
                        }
                    });
        }

        return detailsMovieMutableLiveData;
    }

    public MutableLiveData<ArrayList<MovieLinkModel>> getMovieLinks(){
        if (movieLinksMutableLiveData == null){
            movieLinksMutableLiveData = new MutableLiveData<>();

            request.getMovieLink(selectedMovieDetail).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new Observer<ArrayList<MovieLinkModel>>() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            movieLinkDisposable = d;
                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<MovieLinkModel> movieLinkModels) {
                            movieLinksMutableLiveData.postValue(movieLinkModels);
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            movieLinkDisposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            movieLinkDisposable.dispose();
                        }
                    });
        }

        return movieLinksMutableLiveData;
    }

    @Override
    protected void onCleared() {
        movieDetailDisposable.dispose();
        movieLinkDisposable.dispose();
        super.onCleared();
    }
}
