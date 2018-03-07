package com.framgia.tungvd.soundcloud.data.model;

import com.framgia.tungvd.soundcloud.data.source.Genre;

public class Category {

    public static String ALL_MUSIC = "All Music";
    public static String ALL_AUDIO = "All Audio";
    public static String ALTERNATIVEROCK = "Alternativerock";
    public static String AMBIENT = "Ambient";
    public static String CLASSICAL = "Classical";
    public static String COUNTRY = "Country";

    @Genre private String mGenre;
    private String mName;

    public Category(@Genre String genre, String name) {
        mGenre = genre;
        mName = name;
    }

    public String getGenre() {
        return mGenre;
    }

    public String getName() {
        return mName;
    }
}
