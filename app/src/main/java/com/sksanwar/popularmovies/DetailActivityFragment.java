package com.sksanwar.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sksanwar.popularmovies.Adapter.ReviewAdapter;
import com.sksanwar.popularmovies.Adapter.TrailerAdapter;
import com.sksanwar.popularmovies.FetchMoviesTask.FetchReviews;
import com.sksanwar.popularmovies.FetchMoviesTask.FetchTrailers;
import com.sksanwar.popularmovies.Model.Movie;
import com.sksanwar.popularmovies.Model.MovieContract;
import com.sksanwar.popularmovies.Model.Review;
import com.sksanwar.popularmovies.Model.Trailer;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by sksho on 15-Jun-17.
 */

public class DetailActivityFragment extends Fragment {
    static final String MOVIE_OBJECT = "movie_object";
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private ContentResolver resolver;
    private ArrayList<Trailer> mTrailerArrayList;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    public static DetailActivityFragment newInstance(Movie movie) {
        DetailActivityFragment detailFragment = new DetailActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_OBJECT, movie);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_activity_fragment, container, false);

        Movie movie = null;
        Bundle bundle = getArguments();

        if (bundle != null) {
            movie = bundle.getParcelable(MOVIE_OBJECT);
        } else if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(MOVIE_OBJECT);
        }

        resolver = getActivity().getContentResolver();

        if (movie != null) {

            final String title = movie.getTitle();
            //getActivity().setTitle(title);
            final String movie_id = movie.getId();
            final String poster = movie.getPosterPath();
            final String backdrop = movie.getBackdrops();
            final String overview = movie.getOverView();
            final String vote_average = movie.getVoteAverage();
            final String release_date = movie.getReleaseDate();

            final View view = rootView.findViewById(R.id.details_layout);
            view.setVisibility(View.VISIBLE);

            TextView detailTitle = (TextView) rootView.findViewById(R.id.details_title);
            TextView detailReleaseYear = (TextView) rootView.findViewById(R.id.details_release_year);
            TextView detailReleaseMonth = (TextView) rootView.findViewById(R.id.details_release_month);
            TextView detailVoteAverage = (TextView) rootView.findViewById(R.id.details_vote_average);
            TextView detailOverview = (TextView) rootView.findViewById(R.id.details_overview);
            ImageView detailBackdrops = (ImageView) rootView.findViewById(R.id.details_backdrop);
            ImageView detailThumbnail = (ImageView) rootView.findViewById(R.id.details_thumbnail);
            final Button favoriteButton = (Button) rootView.findViewById(R.id.favorite_button);

            detailTitle.setText(movie.getTitle());
            detailReleaseYear.setText(getYear((movie.getReleaseDate())));
            detailReleaseMonth.setText(getMonth(movie.getReleaseDate()));
            detailVoteAverage.setText(movie.getVoteAverage() + "/10");
            detailOverview.setText(movie.getOverView());

            Picasso.with(rootView.getContext())
                    .load(movie.getBackdrops())
                    .into(detailBackdrops);
            Picasso.with(rootView.getContext())
                    .load(movie.getPosterPath())
                    .into(detailThumbnail);

            if (isRowExist(movie_id)) {
                favoriteButton.setText(R.string.unfavorite_Button);
            }

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isRowExist(movie_id)) {
                        addMovie(title,
                                movie_id,
                                poster,
                                backdrop,
                                overview,
                                vote_average,
                                release_date);
                        favoriteButton.setText(R.string.unfavorite_Button);
                    } else {
                        removeMovie(movie_id);
                        favoriteButton.setText(R.string.favorite_Button);
                    }
                }
            });

            trailerView(movie, rootView);
            reviewView(movie, rootView);
        }
        return rootView;
    }

    private String getYear(String date) {
        return date.substring(0, 4);
    }

    private String getMonth(String date) {
        String monthNum = date.substring(5, 7);
        int num = Integer.parseInt(monthNum);
        return new DateFormatSymbols().getMonths()[num - 1];
    }

    //Helper method to check if the row is exists
    private boolean isRowExist(String movieId) {
        Cursor cursor = resolver.query(
                MovieContract.MovieEntry.CONTENT_URI, null,
                MovieContract.MovieEntry.MOVIE_ID + " = ?",
                new String[]{movieId}, null);

        if (cursor != null && cursor.getCount() <= 0) {
            Log.d(LOG_TAG, "isRowExist: doesn't exist " + cursor.getCount());
            cursor.close();
            return false;
        } else {
            Log.d(LOG_TAG, "isRowExist: exists " + cursor.getCount());
            cursor.close();
            return true;
        }
    }

    //Method to add movies
    private void addMovie(String title, String movie_id, String poster, String backdrop,
                          String overview, String vote_average, String release_date) {

        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.TITLE, title);
        values.put(MovieContract.MovieEntry.MOVIE_ID, movie_id);
        values.put(MovieContract.MovieEntry.POSTER, poster);
        values.put(MovieContract.MovieEntry.BACKDROP, backdrop);
        values.put(MovieContract.MovieEntry.OVERVIEW, overview);
        values.put(MovieContract.MovieEntry.VOTE_AVERAGE, vote_average);
        values.put(MovieContract.MovieEntry.DATE, release_date);

        resolver.insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    private void removeMovie(String movie_id) {
        resolver.delete(MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_ID + " = ?",
                new String[]{movie_id});
    }

    //TrailerView Holder
    private void trailerView(Movie movie, View view) {

        RecyclerView trailerRecyclerView =
                (RecyclerView) view.findViewById(R.id.trailer_recyclerview);
        trailerRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trailerRecyclerView.setLayoutManager(layoutManager);

        mTrailerArrayList = new ArrayList<>();

        try {
            mTrailerArrayList =
                    new FetchTrailers().execute(movie.getId()).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        trailerRecyclerView.setAdapter(new TrailerAdapter(view.getContext(), mTrailerArrayList));
    }


    private void reviewView(Movie movie, View view) {

        RecyclerView reviewRecyclerView =
                (RecyclerView) view.findViewById(R.id.review_recyclerview);
        reviewRecyclerView.setHasFixedSize(true);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Review> mReviewArrayList = new ArrayList<>();

        try {
            mReviewArrayList =
                    new FetchReviews().execute(movie.getId()).get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        reviewRecyclerView.setAdapter(new ReviewAdapter(mReviewArrayList));
    }
}
