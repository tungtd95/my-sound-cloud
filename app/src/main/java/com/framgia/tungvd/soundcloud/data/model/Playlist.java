package com.framgia.tungvd.soundcloud.data.model;

import java.util.ArrayList;

public class Playlist {
    private long mId;
    private String mName;
    private ArrayList<Track> mTracks;

    public Playlist(long id, String name, ArrayList<Track> tracks) {
        mId = id;
        mName = name;
        mTracks = tracks;
    }

    public Playlist(long id, String name) {
        mId = id;
        mName = name;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Track> getTracks() {
        return mTracks;
    }
}
