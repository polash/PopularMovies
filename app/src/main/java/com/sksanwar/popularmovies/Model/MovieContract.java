package com.sksanwar.popularmovies.Model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Content that will be used in sqlite
 */

public class MovieContract {

    // Name of the content provider
    public static final String CONTENT_AUTHORITY = "com.sksanwar.popularmovies";

    // add scheme to the content provide name
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // location of the table
    public static final String PATH_MOVIE = "movie";

    /* Inner class that setups up movie table structure */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        public static final String MOVIE_ID = "movie_id";

        public static final String TITLE = "title";

        public static final String POSTER = "poster";

        public static final String BACKDROP = "backdrop";

        public static final String OVERVIEW = "overview";

        public static final String VOTE_AVERAGE = "vote_average";

        public static final String DATE = "date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
