package com.mahmoud.seemov.provider;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.mahmoud.seemov.Data.MovieDao;
import com.mahmoud.seemov.Data.MovieDatabase;
import com.mahmoud.seemov.Data.MovieRepository;
import com.mahmoud.seemov.Data.MovieViewModel;
import com.mahmoud.seemov.Models.Movie;
import com.mahmoud.seemov.provider.MovieContract.MovieEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class FavoriteItemContentProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDB mMovieDB;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES + "/*",
                FAVORITE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mMovieDB = new MovieDB(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDB.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        if (match == FAVORITES) {
            cursor = db.query(MovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//
//        final SQLiteDatabase db = mMovieDB.getWritableDatabase();
//
//        int match = sUriMatcher.match(uri);
//        Uri returnUri;
//
//        if (match == FAVORITES) {
//            long id = db.insert(MovieEntry.TABLE_NAME,
//                    null, values);
//            if (id > 0) {
//                returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
//            } else {
//                throw new SQLException("Failed to insert row into " + uri);
//            }
//        } else {
//            throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        return returnUri;
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        final SQLiteDatabase db = mMovieDB.getWritableDatabase();
//        int match = sUriMatcher.match(uri);
//
//        int favoritesDeleted;
//
//        switch (match) {
//            case FAVORITES:
//                favoritesDeleted = db.delete(MovieEntry.TABLE_NAME,
//                        null, null);
//                break;
//            case FAVORITE_WITH_ID:
//                String id = uri.getPathSegments().get(1);
//                favoritesDeleted = db.delete(MovieEntry.TABLE_NAME,
//                        MovieEntry.COLUMN_ID + " = ?", new String[]{id});
//                break;
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
//        if (favoritesDeleted != 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
//        return favoritesDeleted;
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
