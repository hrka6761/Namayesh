package com.example.namayesh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namayesh.databinding.ActivitySplashBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    SharedPreferences userInfos;
    Intent mainIntent;
    Intent loginIntent;
    Timer timer;
    public static MutableLiveData<Integer> networkStatus;
    NamayeshReciever namayeshReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        timer = new Timer();

        userInfos = getSharedPreferences("userInfos", MODE_PRIVATE);
        mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        loginIntent = new Intent(SplashActivity.this, RegisterLoginActivity.class);


        if (networkStatus == null) {
            networkStatus = new MutableLiveData<>();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1002);
        } else {

            namayeshReciever = new NamayeshReciever();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(namayeshReciever, intentFilter);


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!userInfos.getString("name", "noUserLogin").equals("noUserLogin")) {
                        startActivity(mainIntent);
                    } else {
                        startActivity(loginIntent);
                    }
                    timer.purge();
                    timer.cancel();
                    finish();
                }
            }, 3000, 1);
        }


        networkStatus.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    binding.online.setVisibility(View.GONE);
                    binding.offline.setVisibility(View.VISIBLE);
                    animate(binding.offline, 300);
                } else {
                    binding.offline.setVisibility(View.GONE);
                    binding.online.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1002 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            namayeshReciever = new NamayeshReciever();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(namayeshReciever, intentFilter);


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!userInfos.getString("name", "noUserLogin").equals("noUserLogin")) {
                        startActivity(mainIntent);
                    } else {
                        startActivity(loginIntent);
                    }
                    timer.purge();
                    timer.cancel();
                    finish();
                }
            }, 3000, 1);
        }else {
            Toast.makeText(this, "you cannot use this app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(namayeshReciever);
        super.onDestroy();
    }

    public void animate(TextView textView, int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        textView.setAnimation(alphaAnimation);
    }
}