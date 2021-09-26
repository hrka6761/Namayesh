package com.example.namayesh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.example.namayesh.R;
import com.example.namayesh.databinding.SlideHomeLayoutBinding;
import com.example.namayesh.models.MovieModel;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SliderHomeAdapter extends PagerAdapter {

    SlideHomeLayoutBinding binding;
    ArrayList<MovieModel> MovieList;
    Context context;


    public void updateSlider(ArrayList<MovieModel> MovieList, Context context) {
        this.MovieList = MovieList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return MovieList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @org.jetbrains.annotations.NotNull View view, @NonNull @org.jetbrains.annotations.NotNull Object object) {
        return view == object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater,R.layout.slide_home_layout,container,false);
        binding.setMovieModel(MovieList.get(position));
        container.addView(binding.getRoot());
        return binding.getRoot();
    }


    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

}

