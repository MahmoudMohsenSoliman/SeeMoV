package com.mahmoud.seemov.Data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mahmoud.seemov.Models.Movie;


public class DetailViewModel extends ViewModel {

        private final LiveData<Movie> mMovieLiveData;

        public DetailViewModel(Application application,int movieId)
        {
            DetailRepository detailRepository = new DetailRepository(application, movieId);
            mMovieLiveData = detailRepository.searchMovie();
        }


        public LiveData<Movie> searchMovie(){ return mMovieLiveData; }
}
