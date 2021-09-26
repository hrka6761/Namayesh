package com.example.namayesh.viewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.namayesh.models.GenreModel;
import com.example.namayesh.models.MovieModel;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivityViewModel extends AndroidViewModel {

    ApiInterface request = ApiClient.getApiClient().create(ApiInterface.class);

    Disposable movieListDisposable;
    Disposable genreListDisposable;

    MutableLiveData<ArrayList<MovieModel>> movieListMutableLiveData;
    MutableLiveData<ArrayList<GenreModel>> genreListMutableLiveData;

    public MainActivityViewModel(@androidx.annotation.NonNull @NotNull Application application) {
        super(application);
    }


    public MutableLiveData<ArrayList<MovieModel>> getMovieList() {

        if (movieListMutableLiveData == null) {

            movieListMutableLiveData = new MutableLiveData<>();

            request.recieveMovieListFromServer()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new Observer<ArrayList<MovieModel>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            movieListDisposable = d;
                        }

                        @Override
                        public void onNext(@NonNull ArrayList<MovieModel> movieModels) {
                            movieListMutableLiveData.postValue(movieModels);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getApplication(), "failed to connect to the server !!!! \nplease check your connection \nor try later ...", Toast.LENGTH_LONG).show();
                            movieListDisposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            movieListDisposable.dispose();
                        }
                    });
        }


        return movieListMutableLiveData;
    }

    public MutableLiveData<ArrayList<GenreModel>> getGenreList() {

        if (genreListMutableLiveData == null) {

            genreListMutableLiveData = new MutableLiveData<>();

            request.recieveGenresFromServer()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new Observer<ArrayList<GenreModel>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            genreListDisposable = d;
                        }

                        @Override
                        public void onNext(@NonNull ArrayList<GenreModel> genreModels) {
                            genreListMutableLiveData.postValue(genreModels);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getApplication(), "failed to connect to the server !!!! \nplease check your connection \nor try later ...", Toast.LENGTH_LONG).show();
                            genreListDisposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            genreListDisposable.dispose();
                        }
                    });
        }


        return genreListMutableLiveData;

    }


    @Override
    protected void onCleared() {
        movieListDisposable.dispose();
        genreListDisposable.dispose();
        super.onCleared();
    }
}
