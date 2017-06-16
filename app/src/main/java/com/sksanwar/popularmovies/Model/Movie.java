package com.sksanwar.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sksho on 12-May-17.
 */

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String mTitle;
    String mReleaseDate;
    String mPosterPath;
    String mVoteAverage;
    String mOverView;
    String mBackdrops;
    String mId;

    public Movie(String title, String releaseDate, String posterPath,
                 String voteAverage, String overView, String backdrops, String id) {
        this.mId = id;
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mPosterPath = posterPath;
        this.mVoteAverage = voteAverage;
        this.mOverView = overView;
        this.mBackdrops = backdrops;

    }

    protected Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mReleaseDate = in.readString();
        this.mPosterPath = in.readString();
        this.mVoteAverage = in.readString();
        this.mOverView = in.readString();
        this.mBackdrops = in.readString();
        this.mId = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getOverView() {
        return mOverView;
    }

    public String getBackdrops() {
        return mBackdrops;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mVoteAverage);
        dest.writeString(this.mOverView);
        dest.writeString(this.mBackdrops);
        dest.writeString(this.mId);
    }
}
