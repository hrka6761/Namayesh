package com.example.namayesh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namayesh.MovieDetailsActivity;
import com.example.namayesh.R;
import com.example.namayesh.databinding.TopMovieHomeLayoutBinding;
import com.example.namayesh.models.MovieModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopMovieAdapter extends RecyclerView.Adapter<TopMovieAdapter.MovieViewHolder> {

    ArrayList<MovieModel> MovieList;
    Context context;


    public void updateAdapter(ArrayList<MovieModel> MovieList, Context context) {
        this.MovieList = MovieList;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public TopMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TopMovieHomeLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.top_movie_home_layout, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopMovieAdapter.MovieViewHolder holder, int position) {
        holder.binding.setMovieModel(MovieList.get(position));
        holder.binding.itemTopMovieHomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("SelectedMovie", MovieList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MovieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        TopMovieHomeLayoutBinding binding;
        public MovieViewHolder(@NonNull @NotNull TopMovieHomeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
