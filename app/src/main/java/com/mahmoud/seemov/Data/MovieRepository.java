package com.mahmoud.seemov.Data;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mahmoud.seemov.Models.Movie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieRepository {

    private static MovieDao mMovieDao;
    private final LiveData<List<Movie>> mMoviesList;



    MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mMoviesList = mMovieDao.getAllMovies();

    }


     public static void insert(Movie movie)
    {
       new InsertInDatabaseTask().execute(movie);

    }


    public static void delete(Movie movie)
    {
       new DeleteFromDatabaseTask().execute(movie);
    }

    LiveData<List<Movie> >getAllMovies()
    {
        return mMoviesList;
    }



    static class InsertInDatabaseTask extends AsyncTask<Movie,Void,Void>
    {



        @Override
        protected Void doInBackground(Movie... movies) {

            mMovieDao.insert(movies[0]);

            return null;
        }


    }
    static class DeleteFromDatabaseTask extends AsyncTask<Movie,Void,Void>
    {



        @Override
        protected Void doInBackground(Movie... movies) {

            mMovieDao.delete(movies[0]);

            return null;
        }


    }

}
