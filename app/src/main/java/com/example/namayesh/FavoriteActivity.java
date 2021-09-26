package com.example.namayesh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.namayesh.adapters.SelectedMoreMovieAdapter;
import com.example.namayesh.database.FavoriteMovie;
import com.example.namayesh.database.NamayeshDB;
import com.example.namayesh.databinding.ActivityFavoriteBinding;
import com.example.namayesh.models.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    ActivityFavoriteBinding binding;
    NamayeshDB namayeshDB;
    ArrayList<MovieModel> favoriteMovieList;
    SelectedMoreMovieAdapter selectedMoreMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

        namayeshDB = NamayeshDB.getInstance(this);
        favoriteMovieList = new ArrayList<>();

        binding.searchFavoriteActivity.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtToolbarTitleFavoriteActivity.setVisibility(View.GONE);
            }
        });

       List<FavoriteMovie> favoriteMovies = namayeshDB.favoriteDao().getAllFavorite();

        for (int i = 0; i < favoriteMovies.size(); i++) {
            MovieModel movieModel = new MovieModel(favoriteMovies.get(i).getMovie_id(),
                    favoriteMovies.get(i).getName(),
                    favoriteMovies.get(i).getLink_img(),
                    favoriteMovies.get(i).getDirector(),
                    favoriteMovies.get(i).getRate_imdb(),
                    favoriteMovies.get(i).getTime(),
                    favoriteMovies.get(i).getPublish_date(),
                    favoriteMovies.get(i).getCategory(),
                    favoriteMovies.get(i).getRank());
            favoriteMovieList.add(movieModel);
        }

        selectedMoreMovieAdapter = new SelectedMoreMovieAdapter();
        selectedMoreMovieAdapter.updateAdapter(favoriteMovieList, FavoriteActivity.this);
        binding.rvFavoriteActivity.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this
                , LinearLayoutManager.VERTICAL, false));
        binding.rvFavoriteActivity.setHasFixedSize(true);
        binding.rvFavoriteActivity.setAdapter(selectedMoreMovieAdapter);



    }
}