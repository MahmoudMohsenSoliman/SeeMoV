package com.mahmoud.seemov.MainActivity;

import android.content.Context;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mahmoud.seemov.BuildConfig;
import com.mahmoud.seemov.MainActivity.MovieAdapter.MovieAdapterViewHolder;
import com.mahmoud.seemov.Models.Movie;
import com.mahmoud.seemov.Models.Review;
import com.mahmoud.seemov.Models.Trailer;
import com.mahmoud.seemov.Retrofit.ReviewResponse;
import com.mahmoud.seemov.Retrofit.TMDBClient;
import com.mahmoud.seemov.Retrofit.TMDBInterface;
import com.mahmoud.seemov.Retrofit.TrailerResponse;
import com.mahmoud.seemov.Utilities.MovieNetworkUtils;
import com.mahmoud.seemov.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapterViewHolder> implements Parcelable {

    private List<Movie> mMovies;
    private Context mContext;
    private GridItemClickListener mOnClickListener;



    MovieAdapter(Context context, GridItemClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
    }

    protected MovieAdapter(Parcel in) {
        mMovies = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<MovieAdapter> CREATOR = new Creator<MovieAdapter>() {
        @Override
        public MovieAdapter createFromParcel(Parcel in) {
            return new MovieAdapter(in);
        }

        @Override
        public MovieAdapter[] newArray(int size) {
            return new MovieAdapter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mMovies);
    }


    public interface GridItemClickListener
    {


        void onClick(int position);
    }
    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View rootView = inflater.inflate(R.layout.movie_posters_grid_item,viewGroup,false);
        return new MovieAdapterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int i) {

        Movie movie = mMovies.get(i);
        Uri imageUri =MovieNetworkUtils.makeSuitableImagePath(movie.getPosterPath());



    ImageView movieImage = movieAdapterViewHolder.moviePoster;

        Picasso.get().
                load(imageUri).
                into(movieImage);


        getMovieDataFromTMDB(mMovies.get(i));


    }

    @Override
    public int getItemCount() {

        if(mMovies==null)return 0;
        return mMovies.size();
    }



    public void setMovies(List<Movie> movies) {

        mMovies=movies;
        notifyDataSetChanged();
    }

     List<Movie> getMovies() {
        return mMovies;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        @BindView(R.id.iv_poster_item)
        ImageView moviePoster;

        MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            mOnClickListener.onClick(position);
        }
    }

    private void getMovieDataFromTMDB(Movie movie)
    {
        TMDBInterface tmdbInterface = TMDBClient.getClient().create(TMDBInterface.class);

        Call<TrailerResponse> trailerCall = tmdbInterface.getTrailers(movie.getId(), BuildConfig.ApiKey);
        Call<ReviewResponse> reviewCall = tmdbInterface.getReviews(movie.getId(),BuildConfig.ApiKey);

        trailerCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        ArrayList<Trailer> trailers = response.body().getTrailers();
                        movie.setTrailers(trailers);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                t.printStackTrace();

                //TODO(2) make a textView to imply that there's an error occurred
            }
        });

        reviewCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                if(response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        ArrayList<Review> reviews = response.body().getReviewsOfMovie();
                        movie.setReviews(reviews);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {

                t.printStackTrace();
                //TODO (3) same as (2) but in terms of reviews
            }
        });
    }



}
