package com.sksanwar.popularmovies.FetchMoviesTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sksanwar.popularmovies.BuildConfig;
import com.sksanwar.popularmovies.Model.Trailer;

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
 * Fetches trailer links from the API
 */

public class FetchTrailers extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private static final String LOG_TAG = FetchTrailers.class.getSimpleName();

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

    private ArrayList<Trailer> getTrailerFromJson(String trailerJsonString) throws JSONException {

        final String RESULTS = "results";
        final String MOVIE_DB_KEY = "key";
        final String YOUTUBE_IMAGE_URL_PREFIX = "http://img.youtube.com/vi/";
        final String YOUTUBE_IMAGE_URL_SUFFIX = "/0.jpg";
        final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

        JSONObject trailerJson = new JSONObject(trailerJsonString);
        JSONArray trailerArray = trailerJson.optJSONArray(RESULTS);

        ArrayList<Trailer> trailerArrayList = new ArrayList<>();

        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject trailerObject = trailerArray.getJSONObject(i);

            String key = trailerObject.getString(MOVIE_DB_KEY);

            String image = YOUTUBE_IMAGE_URL_PREFIX + key + YOUTUBE_IMAGE_URL_SUFFIX;
            String link = YOUTUBE_URL + key;

            trailerArrayList.add(new Trailer(image, link));
        }

        return trailerArrayList;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        String trailerJsonString = null;
        InputStream inputStream = null;

        try {
            final String BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String VIDEO_PATH = "videos";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendPath(VIDEO_PATH)
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
                trailerJsonString = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
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
            return getTrailerFromJson(trailerJsonString);
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
