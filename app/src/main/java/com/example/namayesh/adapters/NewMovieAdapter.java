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
import com.example.namayesh.databinding.NewMovieHomeLayoutBinding;
import com.example.namayesh.databinding.TopMovieHomeLayoutBinding;
import com.example.namayesh.models.MovieModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewMovieAdapter extends RecyclerView.Adapter<NewMovieAdapter.MovieViewHolder> {

    ArrayList<MovieModel> MovieList;
    Context context;


    public void updateAdapter(ArrayList<MovieModel> MovieList, Context context) {
        this.MovieList = MovieList;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public NewMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        NewMovieHomeLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.new_movie_home_layout, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewMovieAdapter.MovieViewHolder holder, int position) {
        holder.binding.setMovieModel(MovieList.get(position));
        holder.binding.itemNewMovieHomeLayout.setOnClickListener(new View.OnClickListener() {
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

        NewMovieHomeLayoutBinding binding;
        public MovieViewHolder(@NonNull @NotNull NewMovieHomeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
