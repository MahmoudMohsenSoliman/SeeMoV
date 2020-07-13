package com.mahmoud.seemov.DetailActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mahmoud.seemov.Data.DetailViewModel;
import com.mahmoud.seemov.Data.MovieViewModel;
import com.mahmoud.seemov.DetailActivity.Adapters.ReviewAdapter;
import com.mahmoud.seemov.DetailActivity.Adapters.TrailerAdapter;
import com.mahmoud.seemov.DetailActivity.Adapters.TrailerAdapter.ListItemClickListener;
import com.mahmoud.seemov.Models.Movie;
import com.mahmoud.seemov.R;
import com.mahmoud.seemov.Utilities.MovieNetworkUtils;
import com.mahmoud.seemov.provider.FavoriteItemContentProvider;
import com.mahmoud.seemov.provider.MovieContract;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements ListItemClickListener {

    public static final String EXTRA_MOVIE = "movie to details";
    public static final String EXTRA_TRAILER = "trailer to details";
    public static final String EXTRA_REVIEW = "review to details";
    public static final String EXTRA_BUNDLE = "bundle to details";
    public static final String EXTRA_TWO_PANE = "tablet";

    private TrailerAdapter mTrailerAdapter;
    private static DetailViewModel mDetailViewModel;
    private MovieViewModel mMovieViewModel;
    private boolean twoPane;

    @BindView(R.id.trailer_recyclerView)
    RecyclerView trailerRecyclerView;

    @BindView(R.id.review_recyclerView)
    RecyclerView reviewRecyclerView;

    @BindView(R.id.detail_movie_vote_average)
    RatingBar voteAvgRatingBar;

    @BindView(R.id.detail_movie_title)
    TextView movieTitle;

    @BindView(R.id.detail_plot_synopsis)
    TextView description;

    @BindView(R.id.detail_movie_release_date)
    TextView releaseDate;

    @BindView(R.id.shine_button)
    ShineButton favoriteButton;

    @BindView(R.id.detail_movie_poster)
    ImageView moviePoster;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


       View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

        ButterKnife.bind(rootView);
        if(!twoPane)
       {
           MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
               @Override
               public void onInitializationComplete(InitializationStatus initializationStatus) {
               }
           });
           AdView AdView = rootView.findViewById(R.id.adView);
           AdRequest adRequest = new AdRequest.Builder().build();
           AdView.loadAd(adRequest);
       }


        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);


        populateViews(rootView);
        return rootView;
    }

    private void populateViews(View rootView)
    {

        mTrailerAdapter = new TrailerAdapter(getContext(),this);
        ReviewAdapter reviewAdapter = new ReviewAdapter(getContext());

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());
        trailerLayoutManager.setOrientation(RecyclerView.VERTICAL);
        reviewLayoutManager.setOrientation(RecyclerView.VERTICAL);


        trailerRecyclerView.setLayoutManager( trailerLayoutManager);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);

        trailerRecyclerView.setAdapter(mTrailerAdapter);
        reviewRecyclerView.setAdapter(reviewAdapter);


        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        Bundle movieBundle ;
        Movie movie;

        if(!intent.hasExtra(EXTRA_BUNDLE))
            movieBundle = getArguments();
        else
            movieBundle = intent.getBundleExtra(EXTRA_BUNDLE);


        movie = Objects.requireNonNull(movieBundle).getParcelable(EXTRA_MOVIE);
        Objects.requireNonNull(movie).setTrailers(movieBundle.getParcelableArrayList(EXTRA_TRAILER));
        movie.setReviews(movieBundle.getParcelableArrayList(EXTRA_REVIEW));
        twoPane = movieBundle.getBoolean(EXTRA_TWO_PANE);


        description.setText(movie.getDescription());
        movieTitle.setText(movie.getTitle());

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date dat=new Date();
        try {
            dat = formatter.parse(movie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat f = new SimpleDateFormat("MMMM d, yyyy", Locale.US);


        releaseDate.setText(f.format(dat));
        double voteAvg = movie.getVoteAverage()*0.4f;
        voteAvgRatingBar.setRating((float) voteAvg);
        voteAvgRatingBar.setStepSize(0.25f);
        mTrailerAdapter.setTrailers(movie.getTrailers());
        reviewAdapter.setReviews(movie.getReviews());

        Uri imageUrl = MovieNetworkUtils.makeSuitableImagePath( movie.getPosterPath());
        Picasso.get().load(imageUrl).into(moviePoster);

        mDetailViewModel = new DetailViewModel(getActivity().getApplication(),movie.getId());

        mDetailViewModel.searchMovie().observe(this, new Observer<Movie>() {
                   @Override
                   public void onChanged(Movie movie) {

                       if(movie!=null)
                       {

                           boolean fav = movie.isFav();

                           if(fav)
                           {
                               favoriteButton.setChecked(true);
                           }
                           else
                           {
                               favoriteButton.setChecked(false);
                           }
                       }

                   }
               });


               favoriteButton.setOnClickListener(view -> {

                   boolean fav = favoriteButton.isChecked();


                   if (fav) {
                       movie.setFav(true);
                       mMovieViewModel.insert(movie);

                   } else {
                       movie.setFav(false);
                       mMovieViewModel.delete(movie);

                       if(twoPane)
                       {

                           Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("tag");

                           if(fragment!=null)
                           {
                               getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                           }

                       }



                   }

               });



    }



    @Override
    public void onListItemClick(String path) {
        Uri uri = MovieNetworkUtils.makeYouTubePath(path);

        Intent viewIntent = new Intent(Intent.ACTION_VIEW,uri);

        startActivity(viewIntent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.detail,menu);


        if(mTrailerAdapter.getTrailers().size()>0) {
            String ytPath = MovieNetworkUtils.makeYouTubePath(mTrailerAdapter.getTrailers().get(0).getTrailerPath()).toString();
            Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                    .setChooserTitle("Share to").setType("text/plain").setText(ytPath).getIntent();


            MenuItem menuItem = menu.findItem(R.id.action_share);
            menuItem.setIntent(shareIntent);
        }
    }

}
