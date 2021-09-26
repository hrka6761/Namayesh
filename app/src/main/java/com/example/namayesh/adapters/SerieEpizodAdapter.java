package com.example.namayesh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namayesh.MoviePlayActivity;
import com.example.namayesh.R;
import com.example.namayesh.databinding.SerieEpizodLayoutBinding;
import com.example.namayesh.databinding.TopMovieHomeLayoutBinding;
import com.example.namayesh.models.MovieModel;
import com.example.namayesh.models.SerieEpizodModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SerieEpizodAdapter extends RecyclerView.Adapter<SerieEpizodAdapter.MovieViewHolder> {

    ArrayList<SerieEpizodModel> epizodList;
    Context context;


    public void updateAdapter(ArrayList<SerieEpizodModel> epizodList, Context context) {
        this.epizodList = epizodList;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public SerieEpizodAdapter.MovieViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        SerieEpizodLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.serie_epizod_layout, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SerieEpizodAdapter.MovieViewHolder holder, int position) {
        holder.binding.setSerieEpizodModel(epizodList.get(position));
        holder.binding.imgSerieEpizodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , MoviePlayActivity.class);
                intent.putExtra("movieLink",epizodList.get(position).getSerie_link());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return epizodList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        SerieEpizodLayoutBinding binding;
        public MovieViewHolder(@NonNull @NotNull SerieEpizodLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
