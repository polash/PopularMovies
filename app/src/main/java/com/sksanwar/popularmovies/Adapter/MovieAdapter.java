package com.sksanwar.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sksanwar.popularmovies.Model.Movie;
import com.sksanwar.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Movie Adapter
 */

public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final OnItemClickListener mListener;
    private List<Movie> mMovieList;
    private Context mContext;

    /**
     * Constructor
     *
     * @param context
     * @param movieList
     * @param clickListener
     */
    public MovieAdapter(Context context, List<Movie> movieList, OnItemClickListener clickListener) {
        this.mMovieList = movieList;
        this.mContext = context;
        this.mListener = clickListener;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //layout inflater
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.movie_item_list, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder movieViewHolder, final int position) {

        //Implementation of picasso
        Picasso.with(mContext)
                .load(mMovieList.get(position).getPosterPath())
                .into(movieViewHolder.mImageView);
        movieViewHolder.bind(mMovieList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieList) return 0;
        return mMovieList.size();
    }

    //Clicklistener Interface
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    /**
     * ViewHolder class
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public View mView;

        public MovieViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.poster_imageview);
        }

        public void bind(final Movie movie, final OnItemClickListener listener) {
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }
}