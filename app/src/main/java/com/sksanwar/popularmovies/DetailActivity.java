package com.sksanwar.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sksanwar.popularmovies.Model.Movie;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle args = new Bundle();

            Movie movie = getIntent().getParcelableExtra(DetailActivityFragment.MOVIE_OBJECT);

            DetailActivityFragment detailFragment = DetailActivityFragment.newInstance(movie);

            args.putParcelable(DetailActivityFragment.MOVIE_OBJECT, movie);

            detailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, detailFragment)
                    .commit();
        }
    }
}
