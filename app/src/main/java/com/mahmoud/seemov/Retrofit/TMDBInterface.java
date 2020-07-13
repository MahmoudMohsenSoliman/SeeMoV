package com.mahmoud.seemov.Retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MAHMOUD on 7/28/2018.
 */

public interface TMDBInterface {



    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRated(@Query("api_key") String API_KEY );

    @GET("movie/popular")
    Call<MoviesResponse> getPopular(@Query("api_key") String API_KEY );

    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlaying(@Query("api_key") String API_KEY );

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcoming(@Query("api_key") String API_KEY );

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getTrailers(@Path("id") int id, @Query("api_key") String API_KEY );

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") int id, @Query("api_key") String API_KEY );


    @GET("discover/movie")
    Call<MoviesResponse> getMoviesByGenre(
            @Query("api_key") String API_KEY
            ,@Query("sort_by") String SORT_BY
            ,@Query("with_genres") String GENRE

            );
}
