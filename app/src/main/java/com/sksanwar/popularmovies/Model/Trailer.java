package com.sksanwar.popularmovies.Model;

public class Trailer {
    String mImage;
    String mLink;

    public Trailer(String image,
                   String link) {
        this.mImage = image;
        this.mLink = link;
    }

    public String getImage() {
        return mImage;
    }

    public String getLink() {
        return mLink;
    }
}
