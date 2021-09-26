package com.example.namayesh.retrofitApi;


import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AuthenticationApiClient {

    private static final String URL = "https://api.kavenegar.com/v1/425A7742582B4638623644787369484336516134754F314E69544A665257733271303937704C332B5A424D3D/verify/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }


}
