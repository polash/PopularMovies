package com.sksanwar.popularmovies.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sksanwar.popularmovies.Model.Review;
import com.sksanwar.popularmovies.R;

import java.util.ArrayList;

/**
 * Adapter for showing reviews in the detail Fragment
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> mReviewArrayList;

    public ReviewAdapter(ArrayList<Review> reviewArrayList) {
        mReviewArrayList = reviewArrayList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Layout Inflater
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        holder.mAuthor.setText(mReviewArrayList.get(position).getAuthor());
        holder.mContent.setText(mReviewArrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewArrayList) return 0;
        return mReviewArrayList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthor;
        public TextView mContent;

        public ReviewViewHolder(View view) {
            super(view);
            mContent = (TextView) view.findViewById(R.id.content);
            mAuthor = (TextView) view.findViewById(R.id.author);
        }
    }
}
