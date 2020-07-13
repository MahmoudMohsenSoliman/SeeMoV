package com.mahmoud.seemov.Utilities;

import java.util.ArrayList;
import java.util.List;

import com.mahmoud.seemov.Models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class MovieDBJsonUtils {

    private static final String KEY_RESULTS = "results";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_ID = "id";

    public static List<Movie> getMoviesDataJson(String json) throws JSONException {

        if(json ==null || json.equals(""))
            return null;

        List<Movie> movies=new ArrayList<>();

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);

        int size = results.length();

        for (int i = 0 ; i < size ; i++)
        {
            Movie movie =new Movie();

            JSONObject movieObject = results.getJSONObject(i);

            int id = movieObject.getInt(KEY_ID);
            String title = movieObject.getString(KEY_TITLE);
            String overview = movieObject.getString(KEY_OVERVIEW);
            String  date = movieObject.getString(KEY_RELEASE_DATE);
            double voteAvg = movieObject.getDouble(KEY_VOTE_AVERAGE);
            String poster = movieObject.getString(KEY_POSTER_PATH);

            movie.setId(id);
            movie.setTitle(title);
            movie.setDescription(overview);
            movie.setReleaseDate(date);
            movie.setVoteAverage(voteAvg);
            movie.setPosterPath(poster);

            movies.add(movie);
        }

        return movies;
    }
}
