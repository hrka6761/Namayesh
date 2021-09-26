package com.example.namayesh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.namayesh.MainActivity;
import com.example.namayesh.MovieDetailsActivity;
import com.example.namayesh.R;
import com.example.namayesh.database.FavoriteMovie;
import com.example.namayesh.database.NamayeshDB;
import com.example.namayesh.databinding.SelectedMoreMovieLayoutBinding;
import com.example.namayesh.databinding.TopMovieHomeLayoutBinding;
import com.example.namayesh.models.MovieModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SelectedMoreMovieAdapter extends RecyclerView.Adapter<SelectedMoreMovieAdapter.MovieViewHolder> {

    ArrayList<MovieModel> MovieList;
    Context context;
    NamayeshDB namayeshDB;


    public void updateAdapter(ArrayList<MovieModel> MovieList, Context context) {
        this.MovieList = MovieList;
        this.context = context;

        namayeshDB = NamayeshDB.getInstance(context);
    }


    @NonNull
    @NotNull
    @Override
    public SelectedMoreMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        SelectedMoreMovieLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.selected_more_movie_layout, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SelectedMoreMovieAdapter.MovieViewHolder holder, int position) {
        holder.binding.setMovieModel(MovieList.get(position));
        holder.binding.itemSelectedMoreMovieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("SelectedMovie", MovieList.get(position));
                context.startActivity(intent);
            }
        });

        if (namayeshDB.favoriteDao().isExistsFavorite(Integer.parseInt(MovieList.get(position).getId()))==1){
            holder.binding.imgFavoriteSelectedMoreMovieLayout.setImageResource(R.drawable.favorite);
        }else {
            holder.binding.imgFavoriteSelectedMoreMovieLayout.setImageResource(R.drawable.unfavorite);
        }


        holder.binding.imgFavoriteSelectedMoreMovieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (namayeshDB.favoriteDao().isExistsFavorite(Integer.parseInt(MovieList.get(position).getId()))==1){
                    holder.binding.imgFavoriteSelectedMoreMovieLayout.setImageResource(R.drawable.unfavorite);

                    FavoriteMovie favoriteMovie = namayeshDB.favoriteDao().getFavoriteMovie(MovieList.get(position).getId());
                    namayeshDB.favoriteDao().deletFavorite(favoriteMovie);


                }else {
                    holder.binding.imgFavoriteSelectedMoreMovieLayout.setImageResource(R.drawable.favorite);

                    FavoriteMovie favoriteMovie = new FavoriteMovie();
                    favoriteMovie.setMovie_id(MovieList.get(position).getId());
                    favoriteMovie.setName(MovieList.get(position).getName());
                    favoriteMovie.setLink_img(MovieList.get(position).getLink_img());
                    favoriteMovie.setDirector(MovieList.get(position).getDirector());
                    favoriteMovie.setRate_imdb(MovieList.get(position).getRate_imdb());
                    favoriteMovie.setTime(MovieList.get(position).getTime());
                    favoriteMovie.setPublish_date(MovieList.get(position).getPublish_date());
                    favoriteMovie.setCategory(MovieList.get(position).getCategory());
                    favoriteMovie.setRank(MovieList.get(position).getRank());

                    namayeshDB.favoriteDao().insertFavorite(favoriteMovie);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return MovieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        SelectedMoreMovieLayoutBinding binding;

        public MovieViewHolder(@NonNull @NotNull SelectedMoreMovieLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
