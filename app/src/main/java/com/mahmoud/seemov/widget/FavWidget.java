package com.mahmoud.seemov.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;

import android.content.ComponentName;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


import com.mahmoud.seemov.DetailActivity.DetailActivity;
import com.mahmoud.seemov.DetailActivity.DetailFragment;
import com.mahmoud.seemov.MainActivity.MainActivity;
import com.mahmoud.seemov.Models.Movie;
import com.mahmoud.seemov.Models.Trailer;
import com.mahmoud.seemov.R;
import com.mahmoud.seemov.Utilities.MovieNetworkUtils;
import com.mahmoud.seemov.provider.MovieContract;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class FavWidget extends AppWidgetProvider {
    public static final String ACTION_UPDATE_FAVORITE_WIDGETS ="com.mahmoud.seemov.action.update_favorite_widgets";

    public static final String ACTION_NEXT_MOVIE = "next movie";
    public static final String ACTION_PREV_MOVIE = "prev movie";
    public static final String ACTION_NEXT_TRAILER = "next trailer";
    public static final String ACTION_PREV_TRAILER = "prev trailer";
    public static final String ACTION_PLAY_TRAILER = "play trailer";

    private static ArrayList<Movie> mMovies = null;
    private static ArrayList<Trailer> mTrailers = new ArrayList<>();
    private static int mov_pos = 0;
    private static int trl_pos = 0;
    private static int appWidgetID =-1;
    private static boolean  twoPane =false;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        populateWidgetViews(context,appWidgetManager,appWidgetId);
        Intent intent = new Intent(context,FavWidget.class);
        intent.setAction(ACTION_UPDATE_FAVORITE_WIDGETS);
        Log.e("sdf","mov_pos = "+mov_pos+" ,trl_pos = "+trl_pos);
        context.sendBroadcast(intent);
    }

    public static void populateWidgetViews(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

            appWidgetID =appWidgetId;

            twoPane = context.getResources().getBoolean(R.bool.isTablet);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fav_widget);



            setMovies(context);

            Movie movie = mMovies.get(mov_pos);
            mTrailers = movie.getTrailers();
            movie.getReviews();

            Picasso.get().load(MovieNetworkUtils.makeSuitableImagePath(mMovies.get(mov_pos).getPosterPath())).into(views,R.id.poster_widget,new int[]{appWidgetId});

            mov_pos = sharedPreferences.getInt("mov_pos",0);
            trl_pos = sharedPreferences.getInt("trl_pos",0);

            if(mMovies.size()==0)
            {
                views.setViewVisibility(R.id.next_mov_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.prev_mov_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.next_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.back_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.play_widget,View.INVISIBLE);
            }
            else {
                views.setViewVisibility(R.id.next_mov_widget,View.VISIBLE);
                views.setViewVisibility(R.id.prev_mov_widget,View.VISIBLE);
                views.setViewVisibility(R.id.next_widget,View.VISIBLE);
                views.setViewVisibility(R.id.back_widget,View.VISIBLE);
                views.setViewVisibility(R.id.play_widget,View.VISIBLE);
            }
            if (mTrailers.size()==0)
            {
                views.setViewVisibility(R.id.next_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.back_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.play_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.trailer_title_widget,View.INVISIBLE);
            }
            else {
                views.setViewVisibility(R.id.next_widget,View.VISIBLE);
                views.setViewVisibility(R.id.back_widget,View.VISIBLE);
                views.setViewVisibility(R.id.play_widget,View.VISIBLE);
                views.setViewVisibility(R.id.trailer_title_widget,View.VISIBLE);
           }
           if(mMovies.size()-1 == mov_pos){
                views.setViewVisibility(R.id.next_mov_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.prev_mov_widget,View.VISIBLE);
           }
           if(mov_pos == 0){
                views.setViewVisibility(R.id.prev_mov_widget,View.INVISIBLE);
           }
           if(mov_pos < mMovies.size()-1 )

                views.setViewVisibility(R.id.next_mov_widget,View.VISIBLE);

           if(mov_pos > 0 && mov_pos < mMovies.size()-1){

                views.setViewVisibility(R.id.prev_mov_widget,View.VISIBLE);
           }

           if(mTrailers.size()-1 == trl_pos){

                views.setViewVisibility(R.id.next_widget,View.INVISIBLE);
                views.setViewVisibility(R.id.back_widget,View.VISIBLE);
           }
           if(trl_pos == 0){
                views.setViewVisibility(R.id.back_widget,View.INVISIBLE);

           }
           if(trl_pos < mTrailers.size()-1)views.setViewVisibility(R.id.next_widget,View.VISIBLE);
           if(trl_pos > 0 && trl_pos < mTrailers.size() -1) {

                views.setViewVisibility(R.id.next_widget,View.VISIBLE);
                views.setViewVisibility(R.id.back_widget,View.VISIBLE);
           }


           if(mTrailers.size()>0)
           {
                views.setTextViewText(R.id.trailer_title_widget,mTrailers.get(trl_pos).getTrailerTitle());
           }

            views.setOnClickPendingIntent(R.id.next_mov_widget,getPendingIntent(context,ACTION_NEXT_MOVIE));
            views.setOnClickPendingIntent(R.id.prev_mov_widget,getPendingIntent(context,ACTION_PREV_MOVIE));
            views.setOnClickPendingIntent(R.id.next_widget,getPendingIntent(context,ACTION_NEXT_TRAILER));
            views.setOnClickPendingIntent(R.id.back_widget,getPendingIntent(context,ACTION_PREV_TRAILER));
            views.setOnClickPendingIntent(R.id.play_widget,getPendingIntent(context,ACTION_PLAY_TRAILER));




            Bundle movieBundle = new Bundle();

            movieBundle.putParcelable(DetailFragment.EXTRA_MOVIE,movie);
            movieBundle.putParcelableArrayList(DetailFragment.EXTRA_TRAILER,movie.getTrailers());
            movieBundle.putParcelableArrayList(DetailFragment.EXTRA_REVIEW,movie.getReviews());
            movieBundle.putBoolean(DetailFragment.EXTRA_TWO_PANE, twoPane);

            if(!twoPane)
            {
                if(!movieBundle.isEmpty())
                    views.setOnClickPendingIntent(R.id.widget_layout,getPendingIntent(context, DetailActivity.class, movieBundle));

            }
            else
            {
                if(!movieBundle.isEmpty())
                {

                    views.setOnClickPendingIntent(R.id.widget_layout,getPendingIntent(context, DetailActivity.class,movieBundle));


                }

           }

           // Instruct the widget manager to update the widget
           appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        switch (action)
        {
            case ACTION_NEXT_MOVIE:{
                if(mov_pos<mMovies.size()-1)mov_pos++;
                trl_pos=0;
                sharedPreferences.edit().putInt("mov_pos",mov_pos).apply();
                sharedPreferences.edit().putInt("trl_pos",trl_pos).apply();
                break;
            }
            case ACTION_PREV_MOVIE:{
                if(mov_pos>0)mov_pos--;

                trl_pos=0;
                sharedPreferences.edit().putInt("mov_pos",mov_pos).apply();
                sharedPreferences.edit().putInt("trl_pos",trl_pos).apply();
                break;
            }
            case ACTION_NEXT_TRAILER:
            {
                if(trl_pos<mTrailers.size()-1)trl_pos++;
                sharedPreferences.edit().putInt("trl_pos",trl_pos).apply();
                break;
            }
            case ACTION_PREV_TRAILER:
            {
                if(trl_pos>0)trl_pos--;
                sharedPreferences.edit().putInt("trl_pos",trl_pos).apply();
                break;
            }
            case ACTION_PLAY_TRAILER:{
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(MovieNetworkUtils.makeYouTubePath(mTrailers.get(trl_pos).getTrailerPath()));
                context.startActivity(i);
                break;
            }
            case ACTION_UPDATE_FAVORITE_WIDGETS:{
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager
                        .getAppWidgetIds(new ComponentName(context, FavWidget.class));
                FavWidget.updateWidget(context,appWidgetManager,appWidgetIds);
                //FavWidget.populateWidgetViews(context, appWidgetManager, appWidgetID);


                break;
            }

        }

        super.onReceive(context, intent);

    }

    private static void setMovies(Context context){


        Uri FAVORITE_URI = MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_FAVORITES).build();
        final Cursor[] cursor = new Cursor[1];


        cursor[0] = context.getContentResolver().query(
                FAVORITE_URI,
                null,
                null,
                null,
                null
        );


        if(cursor[0] !=null)
        {
            mMovies = new ArrayList<>();

            cursor[0].moveToFirst();
            Log.e("sdf", DatabaseUtils.dumpCurrentRowToString(cursor[0]));


            cursor[0].moveToNext();
            Log.e("sdf", DatabaseUtils.dumpCurrentRowToString(cursor[0]));

            cursor[0].moveToFirst();
            while (!cursor[0].isAfterLast()){
                Movie movie = new Movie();

                    movie.setId(cursor[0].getInt(0));
                    movie.setTitle(cursor[0].getString(1));
                    movie.setPosterPath(cursor[0].getString(2));
                    movie.setVoteAverage(cursor[0].getDouble(3));
                    movie.setReleaseDate(cursor[0].getString(4));
                    movie.setDescription(cursor[0].getString(5));
                    movie.setFav(cursor[0].getInt(6)>0);
                    movie.setTrailersJson(cursor[0].getString(7));
                    movie.setReviewsJson(cursor[0].getString(8));

                    cursor[0].moveToNext();
                    mMovies.add(movie);

            }

            cursor[0].close();
        }
        else Log.e("sdf","cursor hasn't data");



    }
    public static void updateWidget(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds)
    {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    protected static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context,FavWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    protected static PendingIntent getPendingIntent(Context context, Class<?> cls,Bundle bundle) {
        Intent intent = new Intent(context,cls);
        intent.putExtra(DetailFragment.EXTRA_BUNDLE,bundle);
        intent.putExtra(cls.getName(),"ClassName");
        return PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

