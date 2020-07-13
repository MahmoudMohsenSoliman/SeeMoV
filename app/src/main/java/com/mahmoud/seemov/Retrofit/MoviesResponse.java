package com.mahmoud.seemov.Retrofit;


import com.google.gson.annotations.SerializedName;
import com.mahmoud.seemov.Models.Movie;

import java.util.ArrayList;

/**
 * Created by MAHMOUD on 7/28/2018.
 */

public class MoviesResponse {
    @SerializedName("results")
    private
    ArrayList<Movie> listOfAllMovies;

    public MoviesResponse()
    {
    listOfAllMovies=new ArrayList<>();
    }
    public ArrayList<Movie> getMovies(){return listOfAllMovies;}
    public void setMovies(ArrayList<Movie> M){listOfAllMovies=M;}

}
