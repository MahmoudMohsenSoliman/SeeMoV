package com.mahmoud.seemov.Utilities;


import android.net.Uri;

public class MovieNetworkUtils {


    private static final String IMG_PATH = "https://image.tmdb.org/t/p/w185";
    private static final String YOUTUBE_PATH= "https://youtu.be/";



  public static Uri makeSuitableImagePath(String imgPath)
  {
      return Uri.parse(IMG_PATH).buildUpon()
              .appendEncodedPath(imgPath).build();
  }
  public static Uri makeYouTubePath(String path)
  {
      return Uri.parse(YOUTUBE_PATH).buildUpon().appendPath(path).build();

  }


}
