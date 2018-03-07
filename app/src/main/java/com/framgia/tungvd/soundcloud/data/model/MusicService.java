package com.framgia.tungvd.soundcloud.data.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
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


public class MusicService extends Service implements MusicServiceObservable {

    private static MusicService sInstance;

    private ArrayList<Track> mTracks;
    private MediaPlayer mMediaPlayer;
    private ArrayList<MusicServiceObserver> mMusicServiceObservers;

    private int mCurrentTrackIndex;
    private long mProgress;
    private long mDuration;

    private Setting mSetting;

    /**
     * @return static instance of MusicService class
     */
    public static MusicService getInstance() {
        return sInstance;
    }

    private MusicService() {
    }

    public void setTracks(List<Track> tracks) {
        mTracks = (ArrayList<Track>) tracks;
        notifyTracksChanged();
        handleNewTrack(0);
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

        mSetting = new Setting(LoopMode.OFF, ShuffleMode.OFF);
        sInstance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    /**
     * invoked when user click next song or at the end of one song
     */
    public void handleNext() {
        // TODO: 03/05/18 handle next song event
    }

    /**
     * invoked when user click previous
     */
    public void handlePrevious() {
        // TODO: 03/05/18 handle previous song event
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
            mMediaPlayer.prepareAsync();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleChangeLoopMode() {
        mSetting.changeLoopMode();
        notifyLoopModeChanged();
    }

    public void handleChangeShuffleMode() {
        mSetting.changeShuffleMode();
        notifyShuffleModeChanged();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
            observer.updateProgress(mProgress);
        }
    }

    @Override
    public void notifyTrackChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateTrack(mTracks.get(mCurrentTrackIndex));
        }
    }

    @Override
    public void notifyTracksChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            observer.updateTracks(mTracks);
        }
    }

}
