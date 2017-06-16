package com.sksanwar.popularmovies.FetchMoviesTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sksanwar.popularmovies.BuildConfig;
import com.sksanwar.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Fetch Movies Data and build the URI also checks for network response
 */

public class FetchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String LOG_TAG = FetchMovies.class.getSimpleName();

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<Movie> getMovieDataFromJson(String movieJsonString) throws JSONException {

        //Base URL Constant
        final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185/";
        final String BACKDROPS_IMAGE_URL = "https://image.tmdb.org/t/p/w300/";

        //String variable to fetch movie data from JSON
        final String MOVIE_RESULT = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_VOTE_AVERAGE = "vote_average";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_BACKDROPS = "backdrop_path";

        //Json Object
        JSONObject movieJson = new JSONObject(movieJsonString);

        //Json Array
        JSONArray movieArray = movieJson.optJSONArray(MOVIE_RESULT);

        //Array List Creation
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        //Iterate through each movie list
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObject = movieArray.getJSONObject(i);

            String id = movieObject.getString(MOVIE_ID);
            String title = movieObject.getString(MOVIE_TITLE);
            String release_date = movieObject.getString(MOVIE_RELEASE_DATE);
            String poster_path = movieObject.getString(MOVIE_POSTER_PATH);
            String vote_average = movieObject.getString(MOVIE_VOTE_AVERAGE);
            String overview = movieObject.getString(MOVIE_OVERVIEW);
            String backdrops = movieObject.getString(MOVIE_BACKDROPS);

            //creating movie object to hold the data
            Movie movie = new Movie(title, release_date, BASE_IMAGE_URL + poster_path,
                    vote_average, overview, BACKDROPS_IMAGE_URL + backdrops, id);

            //adding data to ArrayList
            movieArrayList.add(movie);
        }
        return movieArrayList;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String movieJsonString = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String PAGE = "page";
            final String API_KEY = "api_key";

            //creating the Uri
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(PAGE, params[1])
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                movieJsonString = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // If the code didn't successfully get the Movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing inputStream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(movieJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
    }
}