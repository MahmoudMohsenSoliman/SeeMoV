package com.mahmoud.seemov.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class Trailer implements Parcelable
{
    @SerializedName("key")
    private  String mTrailerPath;
    @SerializedName("name")
    private  String mTrailerTitle;

    public Trailer()
    {
        mTrailerPath = "";
        mTrailerTitle = "";
    }

    public Trailer(String trailerTitle , String trailerPath)
    {
        mTrailerTitle = trailerTitle;
        mTrailerPath = trailerPath;
    }



    public String getTrailerPath() {
        return mTrailerPath;
    }

    public void setTrailerPath(String path){mTrailerPath=path;}

    public void setTrailerTitle(String trailerTitle) {
        mTrailerTitle = trailerTitle;
    }

    public String getTrailerTitle() {
        return mTrailerTitle;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrailerPath);
        dest.writeString(mTrailerTitle);
    }

    private Trailer(Parcel in) {
        mTrailerPath = in.readString();
        mTrailerTitle = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
