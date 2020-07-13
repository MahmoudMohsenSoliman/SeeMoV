package com.mahmoud.seemov.Data;

import android.app.Application;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahmoud.seemov.Models.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final LiveData<List<Movie>> mMoviesList;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieRepository movieRepository = new MovieRepository(application);
        mMoviesList = movieRepository.getAllMovies();

    }


    public void insert(Movie movie) { MovieRepository.insert(movie); }

    public void delete(Movie movie){ MovieRepository.delete(movie); }

    public LiveData<List<Movie>> getAllMovies(){ return mMoviesList;}


}
