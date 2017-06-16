package com.sksanwar.popularmovies;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sksanwar.popularmovies.Adapter.MovieAdapter;
import com.sksanwar.popularmovies.FetchMoviesTask.FetchMovies;
import com.sksanwar.popularmovies.Model.Movie;
import com.sksanwar.popularmovies.Model.MovieContract;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by sksho on 14-Jun-17.
 */

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String PAGE_NUMBER = "1";
    private static final int GRID_ITEMS = 2;
    private static final String POPULAR_MOVIES = "popular";
    private static final String TOP_RATED_MOVIES = "top_rated";
    private static final String FAVORITE_MOVIES = "favorite";
    private static final String SORT_ORDER = "sort";
    private ArrayList<Movie> mMovieArrayList = new ArrayList<Movie>();
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPrefSettings;
    private SharedPreferences.Editor mSharedPrefEditor;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_list_view, container, false);

        mSharedPrefSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPrefEditor = mSharedPrefSettings.edit();
        mSharedPrefEditor.apply();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), GRID_ITEMS));

        updateMovieList();

        mRecyclerView.setAdapter(new MovieAdapter(getActivity(),
                mMovieArrayList,
                new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        Log.d(LOG_TAG, "onItemClick " + movie.toString());
                        initiateCallback(movie);
                    }
                }));

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        String string = mSharedPrefSettings.getString(SORT_ORDER, POPULAR_MOVIES);
        if (string.equals(FAVORITE_MOVIES)) {
            getDataFromDB();
            mRecyclerView.setAdapter(new MovieAdapter(getActivity(),
                    mMovieArrayList,
                    new MovieAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Movie movie) {
                            Log.d(LOG_TAG, "onItemClick " + movie.toString());
                            initiateCallback(movie);
                        }
                    }));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popularity:
                mSharedPrefEditor.putString(SORT_ORDER, POPULAR_MOVIES);
                mSharedPrefEditor.apply();
                updateMovieList();
                item.setChecked(true);
                return true;
            case R.id.rating:
                mSharedPrefEditor.putString(SORT_ORDER, TOP_RATED_MOVIES);
                mSharedPrefEditor.apply();
                updateMovieList();
                item.setChecked(true);
                return true;
            case R.id.favorite:
                mSharedPrefEditor.putString(SORT_ORDER, FAVORITE_MOVIES);
                mSharedPrefEditor.apply();
                updateMovieList();
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String sortBy = mSharedPrefSettings.getString(SORT_ORDER, POPULAR_MOVIES);
        switch (sortBy) {
            case POPULAR_MOVIES:
                menu.findItem(R.id.popularity).setChecked(true);
                break;
            case TOP_RATED_MOVIES:
                menu.findItem(R.id.rating).setChecked(true);
                break;
            case FAVORITE_MOVIES:
                menu.findItem(R.id.favorite).setChecked(true);
                break;
        }
    }

    private void updateMovieList() {
        mMovieArrayList = new ArrayList<>();
        String sortBy = mSharedPrefSettings.getString(SORT_ORDER, POPULAR_MOVIES);

        if (sortBy.equals(POPULAR_MOVIES) ||
                sortBy.equals(TOP_RATED_MOVIES)) {

            try {
                mMovieArrayList =
                        new FetchMovies().execute(sortBy, PAGE_NUMBER).get();
            } catch (ExecutionException | InterruptedException ei) {
                ei.printStackTrace();
            }
        } else if (sortBy.equals(FAVORITE_MOVIES)) {
            getDataFromDB();
        }
        mRecyclerView.setAdapter(new MovieAdapter(getActivity(),
                mMovieArrayList,
                new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        Log.d(LOG_TAG, "onItemClick " + movie.toString());
                        initiateCallback(movie);
                    }
                }));
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    //Method to fetch data from DB
    private void getDataFromDB() {
        mMovieArrayList = new ArrayList<>();
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor =
                resolver.query(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                while (cursor.moveToNext()) ;
                {
                    String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.TITLE));
                    String movie_id = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID));
                    String poster = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.POSTER));
                    String backdrop = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.BACKDROP));
                    String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW));
                    String vote_average = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.VOTE_AVERAGE));
                    String release_date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.DATE));

                    Movie movie = new Movie(title, release_date, poster,
                            vote_average, overview, backdrop, movie_id);
                    mMovieArrayList.add(movie);
                }
            }
        }

        if (cursor != null)
            cursor.close();
    }

    public void initiateCallback(Movie movie) {
        ((Callback) getActivity()).onItemSeleted(movie);
    }

    public interface Callback {
        void onItemSeleted(Movie movie);
    }
}
