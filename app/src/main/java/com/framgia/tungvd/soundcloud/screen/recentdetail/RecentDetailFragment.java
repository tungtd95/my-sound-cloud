package com.framgia.tungvd.soundcloud.screen.recentdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;

import java.util.ArrayList;

public class RecentDetailFragment extends BaseFragment
        implements RecentDetailContract.View {

    private RecentDetailContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void updateLoopMode(int loopMode) {
        //no need to implement
    }

    @Override
    public void updateShuffleMode(int shuffleMode) {
        //no need to implement
    }

    @Override
    public void updateProgress(long progress, long duration) {
        //no need to implement
    }

    @Override
    public void updateTrack(@Nullable Track track) {

    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {
        //no need to implement
    }

    @Override
    public void updateState(int playState) {
        //no need to implement
    }

    @Override
    public void updateFirstTime(int loopMode,
                                int shuffleMode,
                                long progress,
                                long duration,
                                @Nullable Track track,
                                ArrayList<Track> tracks, int playState) {
        updateTrack(track);
    }
}
