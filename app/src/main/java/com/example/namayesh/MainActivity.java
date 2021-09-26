package com.example.namayesh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.namayesh.adapters.AnimationAdapter;
import com.example.namayesh.adapters.GenreAdapter;
import com.example.namayesh.adapters.NewMovieAdapter;
import com.example.namayesh.adapters.SeriesMovieAdapter;
import com.example.namayesh.adapters.SliderHomeAdapter;
import com.example.namayesh.adapters.TopMovieAdapter;
import com.example.namayesh.database.NamayeshDB;
import com.example.namayesh.databinding.ActivityMainBinding;
import com.example.namayesh.databinding.HeaderDrawerMenuHomeBinding;
import com.example.namayesh.models.GenreModel;
import com.example.namayesh.models.MovieModel;
import com.example.namayesh.models.User;
import com.example.namayesh.viewModels.MainActivityViewModel;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    MainActivityViewModel mainActivityViewModel;

    CompositeDisposable compositeDisposable;

    SliderHomeAdapter sliderHomeAdapter;
    TopMovieAdapter topMovieAdapter;
    NewMovieAdapter newMovieAdapter;
    AnimationAdapter animationAdapter;
    SeriesMovieAdapter seriesMovieAdapter;
    GenreAdapter genreAdapter;

    Timer timer;

    SharedPreferences userInfos;

    HeaderDrawerMenuHomeBinding headerDrawerMenuHomeBinding;

    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainActivity(this);
        binding.setOnClickViewListenerOnMainActivity(new OnClickViewListenerOnMainActivity());

        View drawerHeaderView = binding.navigation.getHeaderView(0);
        headerDrawerMenuHomeBinding = HeaderDrawerMenuHomeBinding.bind(drawerHeaderView);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        compositeDisposable = new CompositeDisposable();

        userInfos = getSharedPreferences("userInfos", MODE_PRIVATE);

        setGenreRecyclerView();
        recieveMovieList();

        binding.navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@androidx.annotation.NonNull @NotNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this, "nav_profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "nav_home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_search:
                        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(searchIntent);
                        break;
                    case R.id.nav_buy:
                        Intent favoriteIntent = new Intent(MainActivity.this, PayActivity.class);
                        startActivity(favoriteIntent);
                        break;
                    case R.id.nav_genre:
                        Toast.makeText(MainActivity.this, "nav_genre", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_favorite:
                        Intent payIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(payIntent);
                        break;
                    case R.id.nav_exit:
                        SharedPreferences.Editor userInfosEditor = userInfos.edit();
                        userInfosEditor.putString("id", "");
                        userInfosEditor.putString("name", "noUserLogin");
                        userInfosEditor.putString("email", "");
                        userInfosEditor.putString("phone", "");
                        userInfosEditor.putString("password", "");
                        userInfosEditor.putLong("accunt", 0);
                        userInfosEditor.apply();
                        Intent exitIntent = new Intent(MainActivity.this, RegisterLoginActivity.class);
                        startActivity(exitIntent);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        headerDrawerMenuHomeBinding.setUser(new User(userInfos.getString("id", "id"),
                userInfos.getString("name", "name"),
                userInfos.getString("email", "email"),
                userInfos.getString("phone", "phone"),
                userInfos.getString("password", "pssword"),
                userInfos.getLong("accunt", 0) - (System.currentTimeMillis() / 1000)));
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    private void setGenreRecyclerView() {
        mainActivityViewModel.getGenreList().observe(this, new Observer<ArrayList<GenreModel>>() {
            @Override
            public void onChanged(ArrayList<GenreModel> genreModels) {
                genreAdapter = new GenreAdapter();
                genreAdapter.updateAdapter(genreModels, MainActivity.this);
                binding.rvGenreMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this
                        , LinearLayoutManager.HORIZONTAL, false));
                binding.rvGenreMovie.setHasFixedSize(true);
                binding.rvGenreMovie.setAdapter(genreAdapter);
            }
        });
    }

    private void recieveMovieList() {
        ArrayList<MovieModel> slideMovies = new ArrayList<>();
        ArrayList<MovieModel> topMovies = new ArrayList<>();
        ArrayList<MovieModel> newMovies = new ArrayList<>();
        ArrayList<MovieModel> newAnimations = new ArrayList<>();
        ArrayList<MovieModel> newSeries = new ArrayList<>();

        mainActivityViewModel.getMovieList().observe(this, new Observer<ArrayList<MovieModel>>() {
            @Override
            public void onChanged(ArrayList<MovieModel> movieModels) {

                Observable<MovieModel> movieObservable = Observable.fromIterable(movieModels);

                DisposableObserver<MovieModel> slideMovieObserver = new DisposableObserver<MovieModel>() {
                    @Override
                    public void onNext(@NonNull MovieModel movieModel) {
                        slideMovies.add(movieModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        sliderHomeAdapter = new SliderHomeAdapter();
                        sliderHomeAdapter.updateSlider(slideMovies, MainActivity.this);
                        binding.viewPager.setAdapter(sliderHomeAdapter);
                        binding.tabLayout.setupWithViewPager(binding.viewPager, true);
                    }
                };
                DisposableObserver<MovieModel> topMovieObserver = new DisposableObserver<MovieModel>() {
                    @Override
                    public void onNext(@NonNull MovieModel movieModel) {
                        topMovies.add(movieModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        topMovieAdapter = new TopMovieAdapter();
                        topMovieAdapter.updateAdapter(topMovies, MainActivity.this);
                        binding.rvTopMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                , LinearLayoutManager.HORIZONTAL, false));
                        binding.rvTopMovie.setHasFixedSize(true);
                        binding.rvTopMovie.setAdapter(topMovieAdapter);
                    }
                };
                DisposableObserver<MovieModel> newMovieObserver = new DisposableObserver<MovieModel>() {
                    @Override
                    public void onNext(@NonNull MovieModel movieModel) {
                        newMovies.add(movieModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        newMovieAdapter = new NewMovieAdapter();
                        newMovieAdapter.updateAdapter(newMovies, MainActivity.this);
                        binding.rvNewMovie.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                , LinearLayoutManager.HORIZONTAL, false));
                        binding.rvNewMovie.setHasFixedSize(true);
                        binding.rvNewMovie.setAdapter(newMovieAdapter);
                    }
                };
                DisposableObserver<MovieModel> newAnimationObserver = new DisposableObserver<MovieModel>() {
                    @Override
                    public void onNext(@NonNull MovieModel movieModel) {
                        newAnimations.add(movieModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        animationAdapter = new AnimationAdapter();
                        animationAdapter.updateAdapter(newAnimations, MainActivity.this);
                        binding.rvAnimation.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                , LinearLayoutManager.HORIZONTAL, false));
                        binding.rvAnimation.setHasFixedSize(true);
                        binding.rvAnimation.setAdapter(animationAdapter);
                    }
                };
                DisposableObserver<MovieModel> newSeriesObserver = new DisposableObserver<MovieModel>() {
                    @Override
                    public void onNext(@NonNull MovieModel movieModel) {
                        newSeries.add(movieModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        seriesMovieAdapter = new SeriesMovieAdapter();
                        seriesMovieAdapter.updateAdapter(newSeries, MainActivity.this);
                        binding.rvSeries.setLayoutManager(new LinearLayoutManager(MainActivity.this
                                , LinearLayoutManager.HORIZONTAL, false));
                        binding.rvSeries.setHasFixedSize(true);
                        binding.rvSeries.setAdapter(seriesMovieAdapter);
                    }
                };


                compositeDisposable.add(movieObservable
                        .filter(new Predicate<MovieModel>() {
                            @Override
                            public boolean test(MovieModel movieModel) throws Throwable {
                                return movieModel.getCategory().equals("New");
                            }
                        })
                        .take(5)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(slideMovieObserver));

                autoSlidTimer(slideMovies);

                compositeDisposable.add(movieObservable
                        .filter(new Predicate<MovieModel>() {
                            @Override
                            public boolean test(MovieModel movieModel) throws Throwable {
                                return movieModel.getCategory().equals("Top");
                            }
                        })
                        .take(10)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(topMovieObserver));

                compositeDisposable.add(movieObservable
                        .filter(new Predicate<MovieModel>() {
                            @Override
                            public boolean test(MovieModel movieModel) throws Throwable {
                                return movieModel.getCategory().equals("New");
                            }
                        })
                        .take(10)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newMovieObserver));

                compositeDisposable.add(movieObservable
                        .filter(new Predicate<MovieModel>() {
                            @Override
                            public boolean test(MovieModel movieModel) throws Throwable {
                                return movieModel.getCategory().equals("Animation");
                            }
                        })
                        .take(10)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newAnimationObserver));

                compositeDisposable.add(movieObservable
                        .filter(new Predicate<MovieModel>() {
                            @Override
                            public boolean test(MovieModel movieModel) throws Throwable {
                                return movieModel.getCategory().equals("Series");
                            }
                        })
                        .take(10)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(newSeriesObserver));

            }
        });
    }

    public void autoSlidTimer(ArrayList<MovieModel> movieModels) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.viewPager.getCurrentItem() < movieModels.size() - 1) {
                            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                        } else {
                            binding.viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 5000, 5000);
    }

    public static class OnClickViewListenerOnMainActivity {

        public void onClickDrawerHamberMenu(View view, DrawerLayout drawerLayout) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        public void onClickSearch(View view, MainActivity mainActivity) {
            mainActivity.intent = new Intent(view.getContext(), SearchActivity.class);
            view.getContext().startActivity(mainActivity.intent);
        }

        public void onClickTopMovie(View view, MainActivity mainActivity) {
            mainActivity.intent = new Intent(view.getContext(), MovieListActivity.class);
            mainActivity.intent.putExtra("SelectedMovie", "Top");
            view.getContext().startActivity(mainActivity.intent);
        }

        public void onClickNewMovie(View view, MainActivity mainActivity) {
            mainActivity.intent = new Intent(view.getContext(), MovieListActivity.class);
            mainActivity.intent.putExtra("SelectedMovie", "New");
            view.getContext().startActivity(mainActivity.intent);
        }

        public void onClickNewAnimation(View view, MainActivity mainActivity) {
            mainActivity.intent = new Intent(view.getContext(), MovieListActivity.class);
            mainActivity.intent.putExtra("SelectedMovie", "Animation");
            view.getContext().startActivity(mainActivity.intent);
        }

        public void onClickNewSeries(View view, MainActivity mainActivity) {
            mainActivity.intent = new Intent(view.getContext(), MovieListActivity.class);
            mainActivity.intent.putExtra("SelectedMovie", "Series");
            view.getContext().startActivity(mainActivity.intent);
        }

    }


}



