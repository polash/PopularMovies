package com.sksanwar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sksanwar.popularmovies.Model.Movie;

public class MainActivity extends AppCompatActivity
        implements MainActivityFragment.Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mDblPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            mDblPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment())
                        .commit();
            }
        } else {
            mDblPane = false;
        }
    }


    @Override
    public void onItemSeleted(Movie movie) {
        if (mDblPane) {
            DetailActivityFragment detailFragment =
                    DetailActivityFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,
                            detailFragment).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivityFragment.MOVIE_OBJECT, movie);
            startActivity(intent);
        }
    }
}
