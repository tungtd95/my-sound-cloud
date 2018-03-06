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

import java.util.ArrayList;


public class MusicService extends Service implements MusicServiceObservable {

    private ArrayList<Track> mTracks;
    private MediaPlayer mMediaPlayer;
    private ArrayList<MusicServiceObserver> mMusicServiceObservers;

    private int mCurrentTrackIndex;
    private long mProgress;
    private long mDuration;

    private Setting mSetting;

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

    }

    public void handleNext() {
        // TODO: 03/05/18 handle next song event
    }

    public void handlePrevious() {
        // TODO: 03/05/18 handle previous song event
    }

    public void handleNewTrack() {
        // TODO: 03/05/18 handle user click random song
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
