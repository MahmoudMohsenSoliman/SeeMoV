package com.mahmoud.seemov.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class Review implements Parcelable{

    @SerializedName("author")
    private  String mReviewerName ;
    @SerializedName("content")
    private  String mReviewDescription;

    public Review() {
        mReviewerName="";
        mReviewDescription="";
    }

    public Review(String reviewerName, String reviewDescription) {
        mReviewerName = reviewerName;
        mReviewDescription = reviewDescription;
    }
    public String getReviewerName() {
        return mReviewerName;
    }

    public void setReviewerName(String reviewerName) {
        mReviewerName = reviewerName;
    }

    public void setReviewDescription(String reviewDescription) {
        mReviewDescription = reviewDescription;
    }

    public String getReviewDescription() {
        return mReviewDescription;
    }

    private Review(Parcel in) {
        mReviewerName = in.readString();
        mReviewDescription = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mReviewerName);
        dest.writeString(mReviewDescription);
    }
}
