package com.sksanwar.popularmovies.Model;

public class Review {
    String mAuthor;
    String mContent;

    public Review(String author,
                  String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}

