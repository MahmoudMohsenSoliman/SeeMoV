package com.mahmoud.seemov.Retrofit;


import com.google.gson.annotations.SerializedName;
import com.mahmoud.seemov.Models.Review;

import java.util.ArrayList;

/**
 * Created by MAHMOUD on 7/28/2018.
 */

public class ReviewResponse {
    @SerializedName("results")
    private
    ArrayList<Review> reviewsOfMovie;

    public ReviewResponse() {
        this.reviewsOfMovie = new ArrayList<>();
    }

    public ArrayList<Review> getReviewsOfMovie() {
        return reviewsOfMovie;
    }

    public void setReviewsOfMovie(ArrayList<Review> reviewsOfMovie) {
        this.reviewsOfMovie = reviewsOfMovie;
    }
}
