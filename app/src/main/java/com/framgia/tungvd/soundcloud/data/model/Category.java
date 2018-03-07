package com.framgia.tungvd.soundcloud.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.tungvd.soundcloud.data.source.Genre;

public class Category implements Parcelable {

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

    protected Category(Parcel in) {
        mGenre = in.readString();
        mName = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getGenre() {
        return mGenre;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mGenre);
        parcel.writeString(mName);
    }
}
