package com.framgia.tungvd.soundcloud.data.model;

import java.util.ArrayList;

public class Playlist {
    private int mId;
    private String mName;
    private ArrayList<Track> mTracks;

    public Playlist(int id, String name, ArrayList<Track> tracks) {
        mId = id;
        mName = name;
        mTracks = tracks;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Track> getTracks() {
        return mTracks;
    }
}
