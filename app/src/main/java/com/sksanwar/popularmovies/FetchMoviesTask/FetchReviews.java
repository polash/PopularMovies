package com.sksanwar.popularmovies.FetchMoviesTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sksanwar.popularmovies.BuildConfig;
import com.sksanwar.popularmovies.Model.Review;

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
 * Review Fetch Helper Class
 */

public class FetchReviews extends AsyncTask<String, Void, ArrayList<Review>> {

    private static final String LOG_TAG = FetchReviews.class.getSimpleName();

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

    private ArrayList<Review> getReviewsFromJson(String trailerJsonString)
            throws JSONException {

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        JSONObject reviewsJson = new JSONObject(trailerJsonString);
        JSONArray reviewsArray = reviewsJson.optJSONArray(RESULTS);

        ArrayList<Review> reviewsArrayList = new ArrayList<>();

        for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewsObject = reviewsArray.getJSONObject(i);

            String author = reviewsObject.getString(AUTHOR);
            String content = reviewsObject.getString(CONTENT);

            reviewsArrayList.add(new Review(author, content));
        }
        return reviewsArrayList;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String reviewsJsonString = null;

        try {
            final String BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String REVIEWS = "reviews";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendPath(REVIEWS)
                    .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                reviewsJsonString = readFromStream(inputStream);
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
            return getReviewsFromJson(reviewsJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
