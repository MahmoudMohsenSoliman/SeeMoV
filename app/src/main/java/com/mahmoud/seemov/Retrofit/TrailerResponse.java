package com.mahmoud.seemov.Retrofit;


import com.google.gson.annotations.SerializedName;
import com.mahmoud.seemov.Models.Trailer;

import java.util.ArrayList;

/**
 * Created by MAHMOUD on 7/28/2018.
 */

public class TrailerResponse {
    @SerializedName("results")
    private
    ArrayList<Trailer> mTrailers;

    public TrailerResponse() {
        mTrailers = new ArrayList<>();
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
    }
}
