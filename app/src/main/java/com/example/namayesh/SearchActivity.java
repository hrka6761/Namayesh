package com.example.namayesh;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.namayesh.adapters.SelectedMoreMovieAdapter;
import com.example.namayesh.databinding.ActivitySearchBinding;

import com.example.namayesh.models.MovieModel;
import com.example.namayesh.retrofitApi.ApiClient;
import com.example.namayesh.retrofitApi.ApiInterface;
import com.example.namayesh.viewModels.SearchActivityViewModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    SearchActivityViewModel searchActivityViewModel;
    ApiInterface request = ApiClient.getApiClient().create(ApiInterface.class);
    Disposable disposable;
    SelectedMoreMovieAdapter selectedMoreMovieAdapter;
    String search = "byName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setSearchActivity(this);
        binding.setOnClickViewListenerOnSearchActivity(new OnClickViewListenerOnSearchActivity());


        setSupportActionBar(binding.toolbarSearchActivity);

        searchActivityViewModel = new ViewModelProvider(this).get(SearchActivityViewModel.class);


        binding.searchSearchActivity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                request.recieveSearchedMovieFromServer(newText, search)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new Observer<ArrayList<MovieModel>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(@NonNull ArrayList<MovieModel> movieModels) {
                                searchActivityViewModel.getMovieListMutableLiveData().postValue(movieModels);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                searchActivityViewModel.getMovieListMutableLiveData().observe(SearchActivity.this
                                        , new androidx.lifecycle.Observer<ArrayList<MovieModel>>() {
                                            @Override
                                            public void onChanged(ArrayList<MovieModel> movieModels) {
                                                selectedMoreMovieAdapter = new SelectedMoreMovieAdapter();
                                                selectedMoreMovieAdapter.updateAdapter(movieModels, SearchActivity.this);
                                                binding.rvSearchActivity.setLayoutManager(
                                                        new LinearLayoutManager(SearchActivity.this,
                                                                LinearLayoutManager.VERTICAL,
                                                                false));
                                                binding.rvSearchActivity.setHasFixedSize(true);
                                                binding.rvSearchActivity.setAdapter(selectedMoreMovieAdapter);
                                            }
                                        });
                            }
                        });

                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_search_tollbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.byName:
                binding.searchSearchActivity.setQueryHint("search movies by Name");
                search = "byName";
                break;
            case R.id.byDirector:
                binding.searchSearchActivity.setQueryHint("search movies by Director");
                search = "byDirector";
                break;
            case R.id.byDate:
                binding.searchSearchActivity.setQueryHint("search movies by Date");
                search = "byDate";
                break;
            case R.id.byRate:
                binding.searchSearchActivity.setQueryHint("search movies by Rate/IMdb");
                search = "byRate";
                break;
            default:
                binding.searchSearchActivity.setQueryHint("search movies by Name");
                search = "byName";
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    public static class OnClickViewListenerOnSearchActivity {
        public void onClickBack(View view, SearchActivity searchActivity) {
            searchActivity.finish();
        }
    }
}