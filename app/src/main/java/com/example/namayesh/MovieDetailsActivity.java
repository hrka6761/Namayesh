package com.example.namayesh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.namayesh.adapters.ArtistsAdapter;
import com.example.namayesh.adapters.SerieEpizodAdapter;
import com.example.namayesh.databinding.AccuntDialogViewBinding;
import com.example.namayesh.databinding.ActivityMovieDetailsBinding;
import com.example.namayesh.databinding.QualityDialogViewBinding;
import com.example.namayesh.models.DetailsMovieModel;
import com.example.namayesh.models.MovieLinkModel;
import com.example.namayesh.models.MovieModel;
import com.example.namayesh.viewModels.MovieDetailsActivityViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    ActivityMovieDetailsBinding binding;
    QualityDialogViewBinding qualityDialogViewBinding;
    AccuntDialogViewBinding accuntDialogViewBinding;
    MovieDetailsActivityViewModel movieDetailsActivityViewModel;
    ArtistsAdapter artistsAdapter;
    SerieEpizodAdapter serieEpizodAdapter;
    SharedPreferences userInfos;
    AlertDialog.Builder downloadBuilder;
    AlertDialog downloadDialog;
    AlertDialog.Builder accuntBuilder;
    AlertDialog accuntDialog;
    String choosenLinkDownload;
    DownloadManager downloadManager;
    DownloadManager.Request downloadRequest;
    Uri uri;

    public MovieModel selectedMovie;
    public DetailsMovieModel selectedMovieDetails;

    OnClickViewListenerOnMoviedetailsActivity onClickViewListenerOnMoviedetailsActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        onClickViewListenerOnMoviedetailsActivity = new OnClickViewListenerOnMoviedetailsActivity();
        binding.setMovieDetailsActivity(this);
        binding.setOnClickViewListenerOnMoviedetailsActivity(onClickViewListenerOnMoviedetailsActivity);

        LayoutInflater downloadInflater = LayoutInflater.from(MovieDetailsActivity.this);
        ViewGroup downloadViewGroup = findViewById(android.R.id.content);
        qualityDialogViewBinding = DataBindingUtil.inflate(downloadInflater, R.layout.quality_dialog_view, downloadViewGroup, false);
        qualityDialogViewBinding.setMovieDetailsActivity(this);
        qualityDialogViewBinding.setOnClickViewListenerOnMoviedetailsActivity(onClickViewListenerOnMoviedetailsActivity);

        LayoutInflater accuntInflater = LayoutInflater.from(MovieDetailsActivity.this);
        ViewGroup accuntViewGroup = findViewById(android.R.id.content);
        accuntDialogViewBinding = DataBindingUtil.inflate(accuntInflater, R.layout.accunt_dialog_view, accuntViewGroup, false);
        accuntDialogViewBinding.setMovieDetailsActivity(this);
        accuntDialogViewBinding.setOnClickViewListenerOnMoviedetailsActivity(onClickViewListenerOnMoviedetailsActivity);

        selectedMovie = getIntent().getParcelableExtra("SelectedMovie");
        userInfos = getSharedPreferences("userInfos", MODE_PRIVATE);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        movieDetailsActivityViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @NotNull
            @Override
            public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
                return (T) new MovieDetailsActivityViewModel(getApplication(), selectedMovie.getId());
            }
        })
                .get(MovieDetailsActivityViewModel.class);


        movieDetailsActivityViewModel.getDetailsMovie().observe(this, new Observer<DetailsMovieModel>() {
            @Override
            public void onChanged(DetailsMovieModel detailsMovieModel) {
                selectedMovieDetails = detailsMovieModel;
                Glide.with(MovieDetailsActivity.this)
                        .load(detailsMovieModel.getLink_img())
                        .into(binding.imgHeaderMovieDetailActivity);

                binding.txtDescriptionMovieDetailActivity.setText(detailsMovieModel.getDescription());

                artistsAdapter = new ArtistsAdapter();
                artistsAdapter.updateAdapter(detailsMovieModel.getArtistList(), MovieDetailsActivity.this);
                binding.rvArtistsMovieDetailActivity.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this
                        , LinearLayoutManager.HORIZONTAL, false));
                binding.rvArtistsMovieDetailActivity.setHasFixedSize(true);
                binding.rvArtistsMovieDetailActivity.setAdapter(artistsAdapter);


                if (selectedMovie.getCategory().equals("Series")) {
                    binding.rvPartSeriesDetailActivity.setVisibility(View.VISIBLE);
                    binding.epizods.setVisibility(View.VISIBLE);
                    serieEpizodAdapter = new SerieEpizodAdapter();
                    serieEpizodAdapter.updateAdapter(detailsMovieModel.getEpizodtList(), MovieDetailsActivity.this);
                    binding.rvPartSeriesDetailActivity.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this
                            , LinearLayoutManager.HORIZONTAL, false));
                    binding.rvPartSeriesDetailActivity.setHasFixedSize(true);
                    binding.rvPartSeriesDetailActivity.setAdapter(serieEpizodAdapter);
                }

            }
        });

        movieDetailsActivityViewModel.getMovieLinks().observe(this, new Observer<ArrayList<MovieLinkModel>>() {
            @Override
            public void onChanged(ArrayList<MovieLinkModel> movieLinkModels) {
                qualityDialogViewBinding.radioGroupQualityDialog.setOrientation(LinearLayout.VERTICAL);

                for (int i = 0; i < movieLinkModels.size(); i++) {
                    RadioButton rdbtn = new RadioButton(MovieDetailsActivity.this);
                    rdbtn.setId(i);
                    rdbtn.setText(movieLinkModels.get(i).getquality() + " =>  Volume(" + movieLinkModels.get(i).getVolume() + ")");
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           choosenLinkDownload = movieLinkModels.get(v.getId()).getLink();
                        }
                    });
                    qualityDialogViewBinding.radioGroupQualityDialog.addView(rdbtn);
                }
            }
        });


        createDownloadDialog();
        createAccuntDialog();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            uri = Uri.parse(choosenLinkDownload);
            downloadRequest = new DownloadManager.Request(uri);
//            downloadRequest.setVisibleInDownloadsUi(true);
            downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, selectedMovie.getName()+"_"+System.currentTimeMillis());

            downloadManager.enqueue(downloadRequest);

        } else {
            Toast.makeText(this, "you donot accept /n to Read or write permission", Toast.LENGTH_LONG).show();
        }
    }

    private void createAccuntDialog() {
        accuntBuilder = new AlertDialog.Builder(MovieDetailsActivity.this);
        View view = accuntDialogViewBinding.getRoot();
        accuntBuilder.setView(view);
        accuntDialog = accuntBuilder.create();
    }

    private void createDownloadDialog() {
        downloadBuilder = new AlertDialog.Builder(MovieDetailsActivity.this);
        View view = qualityDialogViewBinding.getRoot();
        downloadBuilder.setView(view);
        downloadDialog = downloadBuilder.create();
    }


    public static class OnClickViewListenerOnMoviedetailsActivity {
        public void onClickback(View view, MovieDetailsActivity movieDetailsActivity) {
            movieDetailsActivity.finish();
        }

        public void onClickComment(View view) {
            Toast.makeText(view.getContext(), "Comment", Toast.LENGTH_LONG).show();
        }

        public void onClickDownload(View view, MovieDetailsActivity movieDetailsActivity) {
            if (movieDetailsActivity.userInfos.getLong("accunt", 5456548) > (System.currentTimeMillis() / 1000)) {
                movieDetailsActivity.downloadDialog.show();
            } else {
                movieDetailsActivity.accuntDialog.show();
            }
        }

        public void onClickPlay(View view, MovieDetailsActivity movieDetailsActivity) {
            if (movieDetailsActivity.userInfos.getLong("accunt", 5456548) > (System.currentTimeMillis() / 1000)) {
                if (movieDetailsActivity.selectedMovieDetails != null) {
                    Intent intent = new Intent(view.getContext(), MoviePlayActivity.class);
                    intent.putExtra("movieLink", movieDetailsActivity.selectedMovieDetails.getLink_movie());
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(movieDetailsActivity, "movie is invalid", Toast.LENGTH_LONG).show();
                }
            } else {
                movieDetailsActivity.accuntDialog.show();
            }
        }

        public void onClickAccuntCancel(View view, MovieDetailsActivity movieDetailsActivity) {
            movieDetailsActivity.accuntDialog.dismiss();
        }

        public void onClickAccuntGoToCredite(View view, MovieDetailsActivity movieDetailsActivity) {
            movieDetailsActivity.accuntDialog.dismiss();
            Intent intent = new Intent(movieDetailsActivity, PayActivity.class);
            movieDetailsActivity.startActivity(intent);

        }

        public void onClickDownloadCancel(View view, MovieDetailsActivity movieDetailsActivity) {
            movieDetailsActivity.downloadDialog.dismiss();
        }

        public void onClickDownloadDownload(View view, MovieDetailsActivity movieDetailsActivity) {
            if (movieDetailsActivity.choosenLinkDownload != null) {
                movieDetailsActivity.downloadDialog.dismiss();
                if (ActivityCompat.checkSelfPermission(movieDetailsActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(movieDetailsActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(movieDetailsActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
                } else {
                    movieDetailsActivity.uri = Uri.parse(movieDetailsActivity.choosenLinkDownload);
                    movieDetailsActivity.downloadRequest = new DownloadManager.Request(movieDetailsActivity.uri);
//                    movieDetailsActivity.downloadRequest.setVisibleInDownloadsUi(true);
                    movieDetailsActivity.downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    movieDetailsActivity.downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, movieDetailsActivity.selectedMovie.getName()+"_"+System.currentTimeMillis());

                    movieDetailsActivity.downloadManager.enqueue(movieDetailsActivity.downloadRequest);
                }
            } else {
                Toast.makeText(movieDetailsActivity, "you have to choose one of movie quality", Toast.LENGTH_LONG).show();
            }
        }
    }
}