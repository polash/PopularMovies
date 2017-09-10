package com.sksanwar.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sksanwar.popularmovies.Model.Trailer;
import com.sksanwar.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for showing trailers in the detail Fragment
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String LOG = Trailer.class.getSimpleName();

    private ArrayList<Trailer> mTrailerArrayList;
    private Context mContext;

    public TrailerAdapter(Context context,
                          ArrayList<Trailer> trailerArrayList) {
        mContext = context;
        mTrailerArrayList = trailerArrayList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mTrailerArrayList.get(position).getLink()));
                v.getContext().startActivity(intent);
            }
        });

        Picasso.with(mContext)
                .load(mTrailerArrayList.get(position).getImage())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailerArrayList) return 0;
        return mTrailerArrayList.size();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_imageview)
        ImageView mImageView;

        public TrailerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
