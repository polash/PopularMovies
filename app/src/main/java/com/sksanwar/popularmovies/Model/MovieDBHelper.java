package com.sksanwar.popularmovies.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sksanwar.popularmovies.Model.MovieContract.MovieEntry;

/**
 * Movie DB Database
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    //Local DB that will store in android device
    static final String DATABASE_NAME = "movie.db";
    //DB version
    private static final int DATABASE_VERSION = 1;

    //Default Constructor
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table creation statement
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieEntry.TITLE + " TEXT NOT NULL, " +
                MovieEntry.POSTER + " TEXT NOT NULL, " +
                MovieEntry.BACKDROP + " TEXT NOT NULL, " +
                MovieEntry.OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieEntry.DATE + " TEXT NOT NULL" +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
