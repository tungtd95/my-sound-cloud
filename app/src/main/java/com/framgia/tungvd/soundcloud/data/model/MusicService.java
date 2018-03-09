package com.framgia.tungvd.soundcloud.data.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObservable;
import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObserver;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.source.setting.Setting;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MusicService extends Service
        implements MusicServiceObservable, MediaPlayer.OnPreparedListener {

    private static MusicService sInstance;

    private ArrayList<Track> mTracks;
    private MediaPlayer mMediaPlayer;
    private ArrayList<MusicServiceObserver> mMusicServiceObservers;

    private int mCurrentTrackIndex;
    private int mProgress;
    private long mDuration;
    private @PlayState int mPlayState;
    private Setting mSetting;

    /**
     * @return static instance of MusicService class
     */
    public static MusicService getInstance() {
        return sInstance;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = (ArrayList<Track>) tracks;
        notifyTracksChanged();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTracks = new ArrayList<>();
        mMediaPlayer = new MediaPlayer();
        mMusicServiceObservers = new ArrayList<>();

        mProgress = 0;
        mDuration = 0;
        mCurrentTrackIndex = 0;
        mPlayState = PlayState.PAUSED;
        mSetting = new Setting(LoopMode.OFF, ShuffleMode.OFF);
        sInstance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void changeMediaState() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayState = PlayState.PAUSED;
        } else {
            mMediaPlayer.start();
            mPlayState = PlayState.PLAYING;
        }
        notifyStateChanged();
    }

    public void handleShuffle() {
        mSetting.changeShuffleMode();
        notifyShuffleModeChanged();
    }

    public void handleLoop() {
        mSetting.changeLoopMode();
        notifyLoopModeChanged();
    }

    /**
     * invoked when user click next song or at the end of one song
     */
    public void handleNext() {
        if (mTracks == null || mMediaPlayer == null) {
            return;
        }
        if (mCurrentTrackIndex == mTracks.size() - 1) {
            mCurrentTrackIndex = 0;
        } else {
            mCurrentTrackIndex++;
        }
        handleNewTrack(mCurrentTrackIndex);
    }

    /**
     * invoked when user click previous
     */
    public void handlePrevious() {
        if (mTracks == null || mMediaPlayer == null) {
            return;
        }
        if (mCurrentTrackIndex == 0) {
            mCurrentTrackIndex = mTracks.size() - 1;
        } else {
            mCurrentTrackIndex--;
        }
        handleNewTrack(mCurrentTrackIndex);
    }

    /**
     * invoked then user click any song
     */
    public void handleNewTrack(int position) {
        mCurrentTrackIndex = position;
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mTracks.get(mCurrentTrackIndex).getSteamUrl());
            mPlayState = PlayState.PREPARING;
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(MusicService.this);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    handleNext();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyTrackChanged();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    @Override
    public void register(MusicServiceObserver observer) {
        mMusicServiceObservers.add(observer);
    }

    @Override
    public void notifyLoopModeChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateLoopMode(mSetting.getLoopMode());
        }
    }

    @Override
    public void notifyShuffleModeChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateShuffleMode(mSetting.getShuffleMode());
        }
    }

    @Override
    public void notifyProgressChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateProgress(mProgress, mDuration);
        }
    }

    @Override
    public void notifyTrackChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateTrack(mTracks.get(mCurrentTrackIndex));
        }
    }

    @Override
    public void notifyStateChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateState(mPlayState);
        }
    }

    @Override
    public void notifyTracksChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateTracks(mTracks);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
        mPlayState = PlayState.PLAYING;
    }
}
