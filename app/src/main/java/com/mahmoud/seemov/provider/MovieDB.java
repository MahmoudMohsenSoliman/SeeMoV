package com.mahmoud.seemov.provider;

import android.content.Context;
import android.content.pm.ComponentInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mahmoud.seemov.provider.MovieContract.MovieEntry;

public class MovieDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie_database";
    private static final int DATABASE_VERSION = 1;


    public MovieDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        final String SQL_CREATE_FAVORITES_TABLE =
//                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
//                        MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
//                        MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
//                        MovieEntry.COLUMN_POSTER_PATH + " TEXT , " +
//                        MovieEntry.COLUMN_VOTE_AVG + " DOUBLE NOT NULL, " +
//                        MovieEntry.COLUMN_RELEASE_DATE+"STRING NOT NULL,"+
//                        MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL,"+
//                        MovieEntry.COLUMN_ISFAV + "BOOLEAN NOT NULL,"+
//                        MovieEntry.COLUMN_TRAILERS + " TEXT , " +
//                        MovieEntry.COLUMN_REVIEWS + " TEXT ) " ;
//
//        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }
}
