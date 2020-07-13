package com.mahmoud.seemov.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MAHMOUD on 7/28/2018.
 */

public class TMDBClient {
    private static final String MOVIE_BASE_URL ="https://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient()
    {
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(MOVIE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
            return retrofit;
    }
}
