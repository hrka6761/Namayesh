package com.example.namayesh.viewModels;

import android.app.Application;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.namayesh.models.MovieModel;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieListActivityViewModel extends AndroidViewModel {

    String selectedMovie;
    MutableLiveData<ArrayList<MovieModel>> movieList;
    MutableLiveData<ArrayList<MovieModel>> searchedMovieList;
    ApiInterface request = ApiClient.getApiClient().create(ApiInterface.class);
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    DisposableObserver<ArrayList<MovieModel>> topMovieObserver = (DisposableObserver<ArrayList<MovieModel>>) getTopMovieObserver();
    DisposableObserver<ArrayList<MovieModel>> newMovieObserver = (DisposableObserver<ArrayList<MovieModel>>) getNewMovieObserver();
    DisposableObserver<ArrayList<MovieModel>> newAnimationObserver = (DisposableObserver<ArrayList<MovieModel>>) getNewAnimationObserver();
    DisposableObserver<ArrayList<MovieModel>> newSeriesObserver = (DisposableObserver<ArrayList<MovieModel>>) getNewSeriesObserver();


    public MovieListActivityViewModel(@NonNull @NotNull Application application, String selectedMovie) {
        super(application);
        this.selectedMovie = selectedMovie;
    }


    public MutableLiveData<ArrayList<MovieModel>> getMovieList() {

        switch (selectedMovie) {
            case "Top":
                if (movieList == null) {
                    movieList = new MutableLiveData<>();

                    compositeDisposable.add(request.recieveSelectedMovieFromServer(selectedMovie)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(topMovieObserver));
                }
                break;

            case "New":
                if (movieList == null) {
                    movieList = new MutableLiveData<>();

                    compositeDisposable.add(request.recieveSelectedMovieFromServer(selectedMovie)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(newMovieObserver));
                }
                break;

            case "Animation":
                if (movieList == null) {
                    movieList = new MutableLiveData<>();

                    compositeDisposable.add(request.recieveSelectedMovieFromServer(selectedMovie)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(newAnimationObserver));
                }
                break;

            case "Series":
                if (movieList == null) {
                    movieList = new MutableLiveData<>();

                    compositeDisposable.add(request.recieveSelectedMovieFromServer(selectedMovie)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribeWith(newSeriesObserver));
                }
                break;

            default:
                Toast.makeText(getApplication(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }

        return movieList;
    }

    public MutableLiveData<ArrayList<MovieModel>> getSearchedMovieList(){
        if (searchedMovieList == null){
            searchedMovieList = new MutableLiveData<>();
        }

        return searchedMovieList;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }


    private Observer<ArrayList<MovieModel>> getTopMovieObserver() {
        return new DisposableObserver<ArrayList<MovieModel>>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<MovieModel> movieModels) {
                movieList.postValue(movieModels);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                compositeDisposable.dispose();
                Toast.makeText(getApplication(), "failed to connect to the server !!!! \nplease check your connection \nor try later ...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observer<ArrayList<MovieModel>> getNewMovieObserver() {
        return new DisposableObserver<ArrayList<MovieModel>>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<MovieModel> movieModels) {
                movieList.postValue(movieModels);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                compositeDisposable.dispose();
                Toast.makeText(getApplication(), "failed to connect to the server !!!! \nplease check your connection \nor try later ...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observer<ArrayList<MovieModel>> getNewAnimationObserver() {
        return new DisposableObserver<ArrayList<MovieModel>>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<MovieModel> movieModels) {
                movieList.postValue(movieModels);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                compositeDisposable.dispose();
                Toast.makeText(getApplication(), "failed to connect to the server !!!! \nplease check your connection \nor try later ...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observer<ArrayList<MovieModel>> getNewSeriesObserver() {
        return new DisposableObserver<ArrayList<MovieModel>>() {
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<MovieModel> movieModels) {
                movieList.postValue(movieModels);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                compositeDisposable.dispose();
                Toast.makeText(getApplication(), "failed to connect to the server !!!! \nplease check your connection \nor try later ...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
