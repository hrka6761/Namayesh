package com.example.namayesh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;


import com.example.namayesh.databinding.ActivityMoviePlayBinding;
import com.example.namayesh.models.DetailsMovieModel;
import com.example.namayesh.viewModels.MoviePlayActivityViewModel;


public class MoviePlayActivity extends AppCompatActivity {

    ActivityMoviePlayBinding binding;
    MoviePlayActivityViewModel moviePlayActivityViewModel;
    MediaController mediaController;
    String movieLink;
    NamayeshReciever namayeshReciever;

    public static MutableLiveData<Boolean> ringingState;


    @SuppressLint({"ClickableViewAccessibility", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_play);
        binding.setMoviePlayActivity(this);
        binding.setOnClickViewListeneronMoviePlayActivity(new OnClickViewListeneronMoviePlayActivity());

        namayeshReciever = new NamayeshReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(namayeshReciever,intentFilter);


        movieLink = getIntent().getExtras().getString("movieLink");
        moviePlayActivityViewModel = new ViewModelProvider(this).get(MoviePlayActivityViewModel.class);

        if (ringingState == null){
            ringingState = new MutableLiveData<>();
            ringingState.setValue(false);
        }

        ringingState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (binding.videoview.isPlaying()){
                        binding.videoview.pause();
                    }
                }else {
                    binding.videoview.start();
                }
            }
        });


        mediaController = new MediaController(MoviePlayActivity.this);
        mediaController.setAnchorView(binding.videoview);
        binding.videoview.requestFocus();
        binding.videoview.setMediaController(mediaController);
        binding.videoview.setVideoURI(Uri.parse(movieLink));

        binding.videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                binding.spinKit.setVisibility(View.GONE);
                binding.videoview.start();
            }
        });


    }




    @Override
    protected void onStart() {
        binding.spinKit.setVisibility(View.VISIBLE);
        moviePlayActivityViewModel.getCurrentPosition().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.videoview.seekTo(integer);
                binding.videoview.start();
            }
        });

        super.onStart();
    }

    @Override
    protected void onPause() {
        moviePlayActivityViewModel.getCurrentPosition().setValue(binding.videoview.getCurrentPosition());
        binding.videoview.pause();
        super.onPause();
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(namayeshReciever);
        super.onDestroy();
    }

    public static class OnClickViewListeneronMoviePlayActivity {
    }

}