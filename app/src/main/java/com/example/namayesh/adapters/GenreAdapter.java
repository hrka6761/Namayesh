package com.example.namayesh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namayesh.R;
import com.example.namayesh.databinding.GenreMovieBinding;
import com.example.namayesh.databinding.TopMovieHomeLayoutBinding;
import com.example.namayesh.models.GenreModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MovieViewHolder> {

    ArrayList<GenreModel> genreList;
    Context context;


    public void updateAdapter(ArrayList<GenreModel> genreList, Context context) {
        this.genreList = genreList;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public GenreAdapter.MovieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        GenreMovieBinding binding = DataBindingUtil.inflate(inflater, R.layout.genre_movie, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GenreAdapter.MovieViewHolder holder, int position) {
        holder.binding.setGenreModel(genreList.get(position));
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        GenreMovieBinding binding;
        public MovieViewHolder(@NonNull @NotNull GenreMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
