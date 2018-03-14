package com.framgia.tungvd.soundcloud.screen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.data.model.PlayState;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObserver;
import com.framgia.tungvd.soundcloud.screen.play.PlayActivity;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnClickListener, MusicServiceObserver {

    protected RelativeLayout mRelativeSubController;
    protected MusicService mMusicService;
    protected ProgressBar mProgressBarMain;
    protected TextView mTextViewTrackName;
    protected TextView mTextViewTrackArtist;
    protected Button mButtonPlay;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mMusicService = ((MusicService.MyBinder) iBinder).getMusicService();
            onMusicServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    protected void initBaseView() {
        mButtonPlay = findViewById(R.id.button_play_main);
        mTextViewTrackName = findViewById(R.id.text_track_name_sub);
        mProgressBarMain = findViewById(R.id.progress_main);
        mTextViewTrackArtist = findViewById(R.id.text_track_artist_sub);
        mButtonPlay.setOnClickListener(this);
    }

    protected void onMusicServiceConnected() {
        mMusicService.register(this);
    }

    protected void initMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    protected void showPlayScreen() {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        overridePendingTransition(
                android.support.design.R.anim.design_bottom_sheet_slide_in,
                android.support.v7.appcompat.R.anim.abc_fade_out
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_sub_controller:
                showPlayScreen();
                break;
            case R.id.button_play_main:
                mMusicService.changeMediaState();
                break;
            default:
                break;
        }
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
        if (track != null) {
            mTextViewTrackName.setText(track.getTitle());
            mTextViewTrackArtist.setText(track.getUserName());
        }
    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {
        //no need to implement
    }

    @Override
    public void updateState(int playState) {
        mProgressBarMain.setVisibility(View.GONE);
        mButtonPlay.setEnabled(true);
        switch (playState) {
            case PlayState.PLAYING:
                mButtonPlay.setBackgroundResource(R.drawable.ic_pause);
                break;
            case PlayState.PAUSED:
                mButtonPlay.setBackgroundResource(R.drawable.ic_play_arrow);
                break;
            case PlayState.PREPARING:
                mButtonPlay.setEnabled(false);
                mButtonPlay.setBackgroundResource(R.drawable.ic_play_arrow);
                mProgressBarMain.setVisibility(View.VISIBLE);
                break;
            default:
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
        updateState(playState);
        updateTrack(track);
    }

}
