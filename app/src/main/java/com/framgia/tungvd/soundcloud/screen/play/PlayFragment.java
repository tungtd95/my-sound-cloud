package com.framgia.tungvd.soundcloud.screen.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObserver;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;

import java.util.ArrayList;

public class PlayFragment extends BaseFragment
        implements PlayContract.View, MusicServiceObserver {

    private Button mButtonPlay;
    private Button mButtonNext;
    private Button mButtonPrevious;
    private TextView mTextViewProgress;
    private TextView mTextViewDuration;
    private SeekBar mSeekBarMain;
    private ImageView mImageViewTrack;

    private void initPlayingView(View view) {
        mButtonPlay = view.findViewById(R.id.button_play);
        mButtonNext = view.findViewById(R.id.button_next);
        mButtonPrevious = view.findViewById(R.id.button_previous);
        mTextViewProgress = view.findViewById(R.id.text_view_progress);
        mTextViewDuration = view.findViewById(R.id.text_view_duration);
        mSeekBarMain = view.findViewById(R.id.seek_bar_main);
        mImageViewTrack = view.findViewById(R.id.image_view_track);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.play_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPlayingView(view);
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

    @Override
    public void updateLoopMode(int loopMode) {

    }

    @Override
    public void updateShuffleMode(int shuffleMode) {

    }

    @Override
    public void updateProgress(long progress) {

    }

    @Override
    public void updateTrack(Track track) {

    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {

    }
}
