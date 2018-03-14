package com.framgia.tungvd.soundcloud.screen.recentplaylist;

import com.framgia.tungvd.soundcloud.data.model.Track;

public class RecentPlaylistPresenter implements RecentPlaylistContract.Presenter {

    private static final String TAG = "RecentPlaylistPresenter";
    private RecentPlaylistContract.View mView;

    @Override
    public void setView(RecentPlaylistContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void download(Track track) {
        // TODO: 03/16/18 implement download track
    }
}
