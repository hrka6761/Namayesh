package com.example.namayesh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.namayesh.adapters.SelectedMoreMovieAdapter;
import com.example.namayesh.databinding.ActivityMovieListBinding;
import com.example.namayesh.models.MovieModel;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;
import com.example.namayesh.viewModels.MovieListActivityViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieListActivity extends AppCompatActivity {

    ActivityMovieListBinding binding;
    MovieListActivityViewModel movieListActivityViewModel;
    SelectedMoreMovieAdapter selectedMoreMovieAdapter;
    ApiInterface request = ApiClient.getApiClient().create(ApiInterface.class);
    Disposable disposable;
    public String selectedMovie;
    String search = "byName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);
        binding.setMovieListActivity(this);
        binding.setOnClickViewListenerOnMovieListActivity(new OnClickViewListenerOnMovieListActivity());

        selectedMovie = getIntent().getExtras().getString("SelectedMovie");

        setSupportActionBar(binding.toolbarSelectedMovieList);
        binding.searchMovieListActivity.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtToolbarTitleSelectedMovieList.setVisibility(View.GONE);
            }
        });

        binding.searchMovieListActivity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                request.recieveSearchedSelectedMovieFromServer(newText, search, selectedMovie)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new io.reactivex.rxjava3.core.Observer<ArrayList<MovieModel>>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<MovieModel> movieModels) {
                                movieListActivityViewModel.getSearchedMovieList().postValue(movieModels);
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                movieListActivityViewModel.getSearchedMovieList().observe(MovieListActivity.this,
                                        new Observer<ArrayList<MovieModel>>() {
                                            @Override
                                            public void onChanged(ArrayList<MovieModel> movieModels) {
                                                selectedMoreMovieAdapter = new SelectedMoreMovieAdapter();
                                                selectedMoreMovieAdapter.updateAdapter(movieModels, MovieListActivity.this);
                                                binding.rvSelectedMovieList.setLayoutManager(new LinearLayoutManager(MovieListActivity.this
                                                        , LinearLayoutManager.VERTICAL, false));
                                                binding.rvSelectedMovieList.setHasFixedSize(true);
                                                binding.rvSelectedMovieList.setAdapter(selectedMoreMovieAdapter);
                                            }
                                        });
                            }
                        });

                return false;
            }
        });


        movieListActivityViewModel = new ViewModelProvider(this
                , new ViewModelProvider.Factory() {
            @NonNull
            @NotNull
            @Override
            public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
                return (T) new MovieListActivityViewModel(getApplication(), selectedMovie);
            }
        })
                .get(MovieListActivityViewModel.class);


        movieListActivityViewModel.getMovieList().observe(this, new Observer<ArrayList<MovieModel>>() {
            @Override
            public void onChanged(ArrayList<MovieModel> movieModels) {
                selectedMoreMovieAdapter = new SelectedMoreMovieAdapter();
                selectedMoreMovieAdapter.updateAdapter(movieModels, MovieListActivity.this);
                binding.rvSelectedMovieList.setLayoutManager(new LinearLayoutManager(MovieListActivity.this
                        , LinearLayoutManager.VERTICAL, false));
                binding.rvSelectedMovieList.setHasFixedSize(true);
                binding.rvSelectedMovieList.setAdapter(selectedMoreMovieAdapter);
            }
        });


    }


    @Override
    protected void onDestroy() {
        if (disposable != null){
            disposable.dispose();
        }
            super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_search_tollbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.byName:
                binding.searchMovieListActivity.setQueryHint("search movies by Name");
                search = "byName";
                break;
            case R.id.byDirector:
                binding.searchMovieListActivity.setQueryHint("search movies by Director");
                search = "byDirector";
                break;
            case R.id.byDate:
                binding.searchMovieListActivity.setQueryHint("search movies by Date");
                search = "byDate";
                break;
            case R.id.byRate:
                binding.searchMovieListActivity.setQueryHint("search movies by Rate/IMdb");
                search = "byRate";
                break;
            default:
                binding.searchMovieListActivity.setQueryHint("search movies by Name");
                search = "byName";
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class OnClickViewListenerOnMovieListActivity {
        public void onClickback(View view, MovieListActivity movieListActivity) {
            movieListActivity.finish();
        }
    }
}