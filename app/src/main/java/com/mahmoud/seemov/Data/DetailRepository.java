package com.mahmoud.seemov.Data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mahmoud.seemov.Models.Movie;

public class DetailRepository {

    private MovieDao mMovieDao;
    private LiveData<Movie> mDetailLiveData;
    public DetailRepository(Application application,int movieId)
    {

        MovieDatabase database = MovieDatabase.getDatabase(application);
        mMovieDao = database.movieDao();

        mDetailLiveData = mMovieDao.searchMovie(movieId);
    }

    LiveData<Movie> searchMovie(){return mDetailLiveData;}
}
