package com.framgia.tungvd.soundcloud.screen.recentplaylist;

public class RecentPlaylistPresenter implements RecentPlaylistContract.Presenter {

    RecentPlaylistContract.View mView;

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
}
