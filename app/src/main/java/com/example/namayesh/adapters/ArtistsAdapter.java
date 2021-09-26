package com.example.namayesh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namayesh.R;
import com.example.namayesh.databinding.AnimationHomeLayoutBinding;
import com.example.namayesh.databinding.ArtistLayoutBinding;
import com.example.namayesh.models.ArtistModel;
import com.example.namayesh.models.MovieModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {

    ArrayList<ArtistModel> artistList;
    Context context;


    public void updateAdapter(ArrayList<ArtistModel> artistList, Context context) {
        this.artistList = artistList;
        this.context = context;
    }


    @NonNull
    @NotNull
    @Override
    public ArtistsAdapter.ArtistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ArtistLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.artist_layout, parent, false);
        return new ArtistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ArtistsAdapter.ArtistViewHolder holder, int position) {
        holder.binding.setArtistModel(artistList.get(position));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        ArtistLayoutBinding binding;
        public ArtistViewHolder(@NonNull @NotNull ArtistLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
