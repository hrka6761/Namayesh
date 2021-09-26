package com.example.namayesh.retrofitApi;

import com.example.namayesh.models.DetailsMovieModel;
import com.example.namayesh.models.GenreModel;
import com.example.namayesh.models.MovieLinkModel;
import com.example.namayesh.models.MovieModel;
import com.example.namayesh.models.User;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("gethomemovie.php")
    Observable<ArrayList<MovieModel>> recieveMovieListFromServer();

    @GET("getgenre.php")
    Observable<ArrayList<GenreModel>> recieveGenresFromServer();

    @POST("getselectedmovie.php")
    Observable<ArrayList<MovieModel>> recieveSelectedMovieFromServer(@Query("category") String category);

    @POST("getmoviedetail.php")
    Observable<DetailsMovieModel> receiveMovieDetail(@Query("selected_movie") String selected_movie);

    @POST("getsearchedmovie.php")
    Observable<ArrayList<MovieModel>> recieveSearchedMovieFromServer(@Query("searchedcharacters") String searchedcharacters,
                                                                     @Query("searchby") String searchby);

    @POST("getsearchedselectedmovie.php")
    Observable<ArrayList<MovieModel>> recieveSearchedSelectedMovieFromServer(@Query("searchedcharacters") String searchedcharacters,
                                                                             @Query("searchby") String searchby,
                                                                             @Query("selectedmovie") String selectedmovie);

    @POST("checkemail.php")
    Call<String> checkEmailInServer(@Query("email") String email);


    @POST("loginuser.php")
    Call<User> loginUser(@Query("email") String email, @Query("password") String password);

    @GET("lookup.json.php")
    Call<String> sendAuthenticationCode(@Query("receptor") String receptor,
                                        @Query("token") int token,
                                        @Query("template") String template);

    @POST("adduser.php")
    Call<User> addUserIntoServer(@Query("name") String name,
                                 @Query("email") String email,
                                 @Query("phone") String phone,
                                 @Query("password") String password,
                                 @Query("accunt") String accunt);

    @GET("getuserinfo.php")
    Call<User> getUserInfo(@Query("email") String email);

    @POST("editaccunt.php")
    Call<User> updateAccunt(@Query("accunt") String accunt, @Query("email") String email);

    @POST("editpassword.php")
    Call<String> updatePassword(@Query("password") String password, @Query("email") String email);

    @POST("getmovielink.php")
    Observable<ArrayList<MovieLinkModel>> getMovieLink(@Query("movie_id")String movie_id);

}
