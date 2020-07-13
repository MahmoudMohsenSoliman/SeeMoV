package com.mahmoud.seemov.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mahmoud.seemov.BuildConfig;
import com.mahmoud.seemov.Data.MovieViewModel;
import com.mahmoud.seemov.DetailActivity.DetailActivity;
import com.mahmoud.seemov.DetailActivity.DetailFragment;
import com.mahmoud.seemov.MainActivity.MovieAdapter.GridItemClickListener;
import com.mahmoud.seemov.Models.Movie;
import com.mahmoud.seemov.R;
import com.mahmoud.seemov.Retrofit.MoviesResponse;
import com.mahmoud.seemov.Retrofit.TMDBClient;
import com.mahmoud.seemov.Retrofit.TMDBInterface;
import com.mahmoud.seemov.Settings.SettingsActivity;


import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GridItemClickListener {


    @BindView(R.id.rv_movie_posters)
      RecyclerView moviesRecyclerView;
    @BindView(R.id.tv_error_message)
      TextView mErrorText;
    @BindView(R.id.pb_loading_rcl)
      ProgressBar mProgressBar;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private  MovieAdapter adapter ;

    private  MovieViewModel mMovieViewModel;


    private boolean twoPane;
    private boolean isFavourite;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        twoPane = findViewById(R.id.fragment_detail_page) != null;
        ButterKnife.bind(this);

        int spanCount = calculateNoOfColumns(getApplicationContext());
        GridLayoutManager layoutManager = new GridLayoutManager(this,spanCount);
        moviesRecyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter(this, this);
        moviesRecyclerView.setAdapter(adapter);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        mFirebaseAnalytics.setCurrentScreen(this,
                getResources().getString(R.string.analytics_current_screen),null);

    }

    @Override
    protected void onResume() {
        super.onResume();



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this );
        final String PREF_LIST_KEY = getString(R.string.pref_sort_key);
        final String defaultValue = getString(R.string.popular_item_values);
        final String PREF_SAVED_KEY = "saved value";
        final String PREF_SAVED_GENRE = "saved genre";
        final String PREF_SAVED_GENRE_SORT = "saved genre sort";

        String sort = sharedPreferences.getString(getString(R.string.sort_key),getString(R.string.genre_by_rating));
        String genre = sharedPreferences.getString(getString(R.string.genre_key),"28");


        String savedGenre =  sharedPreferences.getString(PREF_SAVED_GENRE,"");
        String savedGenreSort = sharedPreferences.getString(PREF_SAVED_GENRE_SORT,"");

        String prefSortType = sharedPreferences.getString(PREF_LIST_KEY, defaultValue);
        String savedSortType = sharedPreferences.getString(PREF_SAVED_KEY,"");

        sharedPreferences.edit()
                .putString(PREF_SAVED_KEY,prefSortType)
                .putString(PREF_SAVED_GENRE,genre)
                .putString(PREF_SAVED_GENRE_SORT,sort)
                .apply();

        boolean condition1 = adapter.getMovies()== null || !Objects.requireNonNull(prefSortType).equals(savedSortType);
        boolean condition2 = Objects.requireNonNull(savedSortType).equals("genre")
                &&((!Objects.requireNonNull(sort).equals(savedGenreSort))
                || !Objects.requireNonNull(genre).equals(savedGenre));

        if( condition1 || condition2  )
         {

             if(twoPane)
             {
                 Fragment fragment = getSupportFragmentManager().findFragmentByTag("tag");

                 if(fragment!=null)
                 {
                     getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                 }

             }

             getMoviesFromTMDB(Objects.requireNonNull(prefSortType));

         }


        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMoviesFromTMDB(prefSortType);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this );
        final String PREF_LIST_KEY = getString(R.string.pref_sort_key);
        final String defaultValue = getString(R.string.popular_item_values);
        final String fav = getString(R.string.favorites_item_values);


        String prefSortType = sharedPreferences.getString(PREF_LIST_KEY, defaultValue);



        if(!fav.equals(prefSortType))
        outState.putParcelableArrayList(DetailFragment.EXTRA_MOVIE, (ArrayList<Movie>) adapter.getMovies());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {


        super.onRestoreInstanceState(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this );
        final String PREF_LIST_KEY = getString(R.string.pref_sort_key);
        final String defaultValue = getString(R.string.popular_item_values);
        final String fav = getString(R.string.favorites_item_values);

        String prefSortType = sharedPreferences.getString(PREF_LIST_KEY, defaultValue);


        if(!fav.equals(prefSortType))
        {
            adapter.setMovies(null);
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(DetailFragment.EXTRA_MOVIE);
            adapter.setMovies(movies);
        }
        else {
            getMoviesFromTMDB(Objects.requireNonNull(prefSortType));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(int position) {

        Movie movie = adapter.getMovies().get(position);

        Bundle movieBundle = new Bundle();

        movieBundle.putParcelable(DetailFragment.EXTRA_MOVIE,movie);
        movieBundle.putParcelableArrayList(DetailFragment.EXTRA_TRAILER,movie.getTrailers());
        movieBundle.putParcelableArrayList(DetailFragment.EXTRA_REVIEW,movie.getReviews());

        movieBundle.putBoolean(DetailFragment.EXTRA_TWO_PANE,twoPane);

        if(!twoPane)
        {
            Intent detailIntent = new Intent(this, DetailActivity.class);

            detailIntent.putExtra(DetailFragment.EXTRA_BUNDLE,movieBundle);

            startActivity(detailIntent);

        }
        else
        {


            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(movieBundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().
                   replace(R.id.fragment_detail_page,fragment,"tag").commit();
        }

    }



    private static int calculateNoOfColumns(Context context) {


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;

    }


    private void getMoviesFromTMDB(String s) {



        moviesRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.INVISIBLE);


        TMDBInterface tmdbInterface = TMDBClient.getClient().create(TMDBInterface.class);

        Call<MoviesResponse> call ;



        switch (s) {
            case "popular":
                adapter.setMovies(null);
                isFavourite = false;
                call = tmdbInterface.getPopular(BuildConfig.ApiKey);
                break;
            case "top_rated":
                adapter.setMovies(null);
                isFavourite = false;
                call = tmdbInterface.getTopRated(BuildConfig.ApiKey);
                break;
            case "upcoming":
                adapter.setMovies(null);
                isFavourite = false;
                call = tmdbInterface.getUpcoming(BuildConfig.ApiKey);
                break;
            case "now_playing":
                adapter.setMovies(null);
                isFavourite = false;
                call = tmdbInterface.getNowPlaying(BuildConfig.ApiKey);
                break;
            case "favorites":
                int movieAdapterSize = adapter.getItemCount();

                isFavourite = true;
                mProgressBar.setVisibility(View.INVISIBLE);
                mErrorText.setVisibility(View.INVISIBLE);
                moviesRecyclerView.setVisibility(View.VISIBLE);


                mMovieViewModel.getAllMovies().observe(MainActivity.this,

                                movies ->
                                {
                                    if(isFavourite)
                                        adapter.setMovies(movies);

                                    if(movieAdapterSize>movies.size())
                                    {
                                        if(twoPane)
                                        {
                                            Fragment fragment = getSupportFragmentManager().findFragmentByTag("tag");

                                            if(fragment!=null)
                                            {
                                                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                            }

                                        }
                                    }
                                }
                        );


                return;

                case "genre":


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this );
                    String sort = sharedPreferences.getString(getString(R.string.sort_key),getString(R.string.genre_by_rating));
                    String genre = sharedPreferences.getString(getString(R.string.genre_key),"28");



                    adapter.setMovies(null);

                    isFavourite = false;
                    call = tmdbInterface.getMoviesByGenre(BuildConfig.ApiKey,sort,genre);


                    break;
            default:

                return;
        }

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {



                if(response.isSuccessful()) {

                    Log.e("sdf", "Response: " + response.body().getMovies().get(0).getTitle());

                    if (response.body() != null) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mErrorText.setVisibility(View.INVISIBLE);
                        moviesRecyclerView.setVisibility(View.VISIBLE);

                        ArrayList<Movie> movies = response.body().getMovies();

                        adapter.setMovies(movies);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {

                mErrorText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                moviesRecyclerView.setVisibility(View.INVISIBLE);

            }
        });


    }




}

