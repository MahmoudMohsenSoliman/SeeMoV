 package com.mahmoud.seemov.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


 @Entity(tableName = "movie")
 public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private int mId;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String mTitle;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String mPosterPath;

    @ColumnInfo(name = "vote_average" )
    @SerializedName("vote_average")
    private double mVoteAverage;

    @ColumnInfo(name="release_date")
    @SerializedName("release_date")
    private String mReleaseDate;

    @ColumnInfo(name="overview")
    @SerializedName("overview")
    private String mDescription;

    @ColumnInfo(name = "isFav")
    private boolean isFav;
    @ColumnInfo(name = "trailers")
    private String mTrailersJson;
    @ColumnInfo(name = "reviews")
    private String mReviewsJson;

    @Ignore
    private ArrayList<Review> mReviews;
    @Ignore
    private ArrayList<Trailer> mTrailers;

    public Movie() {
        mId=0;
        mTitle="";
        mPosterPath="";
        mVoteAverage=0;
        mReleaseDate="";
        mDescription="";
        mTrailersJson="";
        mReviewsJson="";
        mReviews=new ArrayList<>();
        mTrailers = new ArrayList<>();
    }


     public String getTrailersJson() {
         return mTrailersJson;
     }

     public String getReviewsJson() {
         return mReviewsJson;
     }

     public void setTrailersJson(String trailersJson) {
         mTrailersJson = trailersJson;
     }

     public void setReviewsJson(String reviewsJson) {
         mReviewsJson = reviewsJson;
     }

     public void setId(int id) {
        mId = id;
    }

    public int getId(){return mId;}

     public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

     public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

     public ArrayList<Review> getReviews() {
        if(mReviews.size()==0)
        {
            try {
               mReviews= (ArrayList<Review>) retrieveReviewListFromJSON();
            } catch (JSONException e) {
                Log.e("sdf",e.getMessage());
            }
        }
         return mReviews;
     }

     public void setReviews(ArrayList<Review> reviews) {
         mReviews = reviews;
         try {
             if(mReviewsJson==null||mReviewsJson.isEmpty())
             mReviewsJson = treatReviewsAsJSON();
         } catch (JSONException e) {
             Log.e("sdf",e.getMessage());
         }
     }

     public ArrayList<Trailer> getTrailers() {

         if(mTrailers.size()==0)
         {
             try {
                 mTrailers= (ArrayList<Trailer>) retrieveTrailerListFromJSON();
             } catch (JSONException e) {
                 Log.e("sdf",e.getMessage());
             }
         }
         return mTrailers;
     }

     public void setTrailers(ArrayList<Trailer> trailers) {
         mTrailers = trailers;
         try {
             if(mTrailersJson ==null||mTrailersJson.isEmpty() )
             mTrailersJson = treatTrailersAsJSON();
         } catch (JSONException e) {
             Log.e("sdf",e.getMessage());
         }
     }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    private List<Trailer> retrieveTrailerListFromJSON() throws JSONException {
        ArrayList<Trailer> trailers = new ArrayList<>();

        if(!mTrailersJson.isEmpty()){
            JSONArray array = new JSONArray(mTrailersJson);
            int size = array.length();

            for(int i=0;i<size;i++)
            {
                JSONObject object = array.getJSONObject(i);
                String key = object.getString("key");
                String name = object.getString("name");

                Trailer trailer = new Trailer();
                trailer.setTrailerPath(key);
                trailer.setTrailerTitle(name);

                trailers.add(trailer);
            }
        }

        return trailers;
    }

     private List<Review> retrieveReviewListFromJSON() throws JSONException {
         ArrayList<Review> reviews = new ArrayList<>();

         if(!mReviewsJson.isEmpty()){
             JSONArray array = new JSONArray(mReviewsJson);
             int size = array.length();

             for(int i=0;i<size;i++)
             {
                 JSONObject object = array.getJSONObject(i);
                 String author = object.getString("author");
                 String content = object.getString("content");

                 Review review = new Review();
                 review.setReviewerName(author);
                 review.setReviewDescription(content);

                 reviews.add(review);
             }
         }

         return reviews;
     }

    private String treatTrailersAsJSON() throws JSONException {

        String json="";
        if(mTrailers !=null)
        {
            int size = mTrailers.size();
            JSONArray array = new JSONArray();
            for(int i=0;i<size;i++)
            {
                JSONObject object = new JSONObject();
                Trailer trailer = mTrailers.get(i);
                object.put("key",trailer.getTrailerPath());
                object.put("name",trailer.getTrailerTitle());

                array.put(object);
            }

            json = array.toString();
        }

        return json;
    }
    private String treatReviewsAsJSON() throws JSONException {

        String json="";
        if(mReviews !=null)
        {
            int size = mReviews.size();
            JSONArray array = new JSONArray();
            for(int i=0;i<size;i++)
            {
                JSONObject object = new JSONObject();
                Review review = mReviews.get(i);
                object.put("author",review.getReviewerName());
                object.put("content",review.getReviewDescription());

                array.put(object);
            }

            json = array.toString();
        }

        return json;
    }
    // *********< <Parcelable Part> >*********

    private Movie(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mPosterPath = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
        mDescription = in.readString();
         in.readBooleanArray(new boolean[]{isFav});

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mDescription);
        boolean[] booleans = new boolean[]{isFav};
        parcel.writeBooleanArray(booleans);
    }
}
