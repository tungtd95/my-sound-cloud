package com.framgia.tungvd.soundcloud.screen.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;

public class PlayFragment extends BaseFragment implements PlayContract.View{

    private PlayContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showPlayState(int playState) {

    }

    @Override
    public void showShuffleMode(int shuffle) {

    }

    @Override
    public void showLoopMode(int loopMode) {

    }

    @Override
    public void showProgress(int progress) {

    }

    @Override
    public void showTrack(Track track) {

    }
}
