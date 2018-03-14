package com.framgia.tungvd.soundcloud.screen.play;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.data.model.PlayState;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;
import com.framgia.tungvd.soundcloud.screen.main.MainActivity;
import com.framgia.tungvd.soundcloud.util.UsefulFunc;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity
        implements PlayContract.View, View.OnClickListener {

    private static final int PROGRESS_MAX = 100;
    private static final int ONE_SECOND = 1000; /* millisecond */

    private Button mButtonPlay;
    private Button mButtonNext;
    private Button mButtonPrevious;
    private Button mButtonLoop;
    private Button mButtonShuffle;
    private TextView mTextViewProgress;
    private TextView mTextViewDuration;
    private SeekBar mSeekBarMain;
    private ImageView mImageViewTrack;

    private PlayContract.Presenter mPresenter;
    private MusicService mMusicService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
            mMusicService.register(PlayActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void initPlayingView() {
        mButtonPlay = findViewById(R.id.button_play);
        mButtonNext = findViewById(R.id.button_next);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonShuffle = findViewById(R.id.button_shuffle);
        mButtonLoop = findViewById(R.id.button_loop);
        mTextViewProgress = findViewById(R.id.text_view_progress);
        mTextViewDuration = findViewById(R.id.text_view_duration);
        mSeekBarMain = findViewById(R.id.seek_bar_main);
        mImageViewTrack = findViewById(R.id.image_view_track);
        mSeekBarMain.setMax(PROGRESS_MAX);
        mButtonLoop.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
    }

    private void initMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen);
        initPlayingView();
        initMusicService();
        mPresenter = new PlayPresenter();
        mPresenter.setView(this);
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMusicService == null) {
            return;
        }
        mMusicService.unregister(this);
    }

    @Override
    public void onClick(View view) {
        if (mMusicService == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.button_play:
                mMusicService.changeMediaState();
                break;
            case R.id.button_next:
                mMusicService.handleNext();
                break;
            case R.id.button_previous:
                mMusicService.handlePrevious();
                break;
            case R.id.button_shuffle:
                mMusicService.handleShuffle();
                break;
            case R.id.button_loop:
                mMusicService.handleLoop();
                break;
            default:
                break;
        }
    }

    @Override
    public void updateLoopMode(int loopMode) {
        switch (loopMode) {
            case LoopMode.ONE:
                mButtonLoop.setBackgroundResource(R.drawable.ic_repeat_one);
                break;
            case LoopMode.ALL:
                mButtonLoop.setBackgroundResource(R.drawable.ic_repeat_all);
                break;
            case LoopMode.OFF:
                mButtonLoop.setBackgroundResource(R.drawable.ic_repeat_off);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateShuffleMode(int shuffleMode) {
        switch (shuffleMode) {
            case ShuffleMode.ON:
                mButtonShuffle.setBackgroundResource(R.drawable.ic_shuffle_on);
                break;
            case ShuffleMode.OFF:
                mButtonShuffle.setBackgroundResource(R.drawable.ic_shuffle_off);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateProgress(long progress, long duration) {
        long temp = duration + 1;
        mSeekBarMain.setProgress((int) (progress * PROGRESS_MAX / temp));
        mTextViewProgress.setText(UsefulFunc
                .convertProgressToTime(progress / ONE_SECOND));
        mTextViewDuration.setText(UsefulFunc
                .convertProgressToTime(duration / ONE_SECOND));
    }

    @Override
    public void updateTrack(@Nullable Track track) {
        // TODO: 03/13/18 implement later
    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {
        // TODO: 03/13/18 implement later
    }

    @Override
    public void updateState(int playState) {
        mButtonPlay.setClickable(true);
        switch (playState) {
            case PlayState.PLAYING:
                mButtonPlay.setBackgroundResource(R.drawable.ic_pause_circle_filled);
                break;
            case PlayState.PAUSED:
                mButtonPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
                break;
            case PlayState.PREPARING:
                mButtonPlay.setClickable(false);
                mButtonPlay.setBackgroundResource(R.drawable.three_dot_animation);
                AnimationDrawable threeDotAnimation =
                        (AnimationDrawable) mButtonPlay.getBackground();
                threeDotAnimation.start();
                break;
            default:
                mButtonPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
                break;
        }
    }

    @Override
    public void updateFirstTime(int loopMode,
                                int shuffleMode,
                                long progress,
                                long duration,
                                @Nullable Track track,
                                ArrayList<Track> tracks,
                                int playState) {
        updateLoopMode(loopMode);
        updateShuffleMode(shuffleMode);
        updateProgress(progress, duration);
        updateState(playState);
        updateTrack(track);
        updateTracks(tracks);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,
                android.support.design.R.anim.design_bottom_sheet_slide_out);

    }
}
