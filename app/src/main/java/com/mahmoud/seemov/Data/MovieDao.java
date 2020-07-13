package com.mahmoud.seemov.Data;


import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mahmoud.seemov.Models.Movie;

import java.util.List;

@Dao
public interface MovieDao {


    @Insert
    void insert(Movie movie);


    @Delete
    void delete(Movie movie);

    @Query("SELECT * from movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * from movie")
    Cursor selectAllMovies();

    @Query("SELECT * from movie where mId == :movieId")
    LiveData<Movie> searchMovie(int movieId);

}
