package com.framgia.tungvd.soundcloud.data.model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObservable;
import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObserver;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.source.setting.Setting;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MusicService extends Service
        implements MusicServiceObservable, MediaPlayer.OnPreparedListener {

    private static final int CHECK_MEDIA_DELAY = 100; /* time delay when check media's progress*/
    private static MusicService sInstance;

    private ArrayList<Track> mTracks;
    private MediaPlayer mMediaPlayer;
    private ArrayList<MusicServiceObserver> mMusicServiceObservers;

    private int mCurrentTrackIndex;
    private long mProgress;
    private long mDuration;
    private @PlayState
    int mPlayState;
    private Setting mSetting;
    private Track mPlayingTrack;
    private ArrayList<Integer> shuffleList;
    private int mCurrentTrackShuffle;

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

    private void createShuffleList(int size) {
        shuffleList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            shuffleList.add(i);
        }
        Collections.shuffle(shuffleList);
        shuffleList.remove((Integer) mCurrentTrackIndex);
        shuffleList.add(0, mCurrentTrackIndex);
        mCurrentTrackShuffle = 0;
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
        notifyStateChanged();

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    long currentTemp = mMediaPlayer.getCurrentPosition();
                    if (currentTemp != mProgress) {
                        mProgress = currentTemp;
                        mDuration = mMediaPlayer.getDuration();
                        notifyProgressChanged();
                    }
                }
                handler.postDelayed(this, CHECK_MEDIA_DELAY);
            }
        };
        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void changeMediaState() {
        if (mMediaPlayer == null) {
            return;
        }
        switch (mPlayState) {
            case PlayState.PLAYING:
                mMediaPlayer.pause();
                mPlayState = PlayState.PAUSED;
                notifyStateChanged();
                break;
            case PlayState.PAUSED:
                mMediaPlayer.start();
                mPlayState = PlayState.PLAYING;
                notifyStateChanged();
                break;
            default:
                break;
        }
    }

    public void handleShuffle() {
        mSetting.changeShuffleMode();
        if (mSetting.getShuffleMode() == ShuffleMode.ON) {
            createShuffleList(mTracks.size());
        }
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
        if (mTracks == null || mMediaPlayer == null || mTracks.size() == 0) {
            return;
        }
        if (mSetting.getShuffleMode() == ShuffleMode.ON) {
            if (mCurrentTrackShuffle >= shuffleList.size() - 1) {
                if (mSetting.getLoopMode() != LoopMode.ALL) {
                    return;
                }
                createShuffleList(mTracks.size());
            }
            if (mCurrentTrackShuffle < shuffleList.size() - 1) {
                mCurrentTrackShuffle++;
            }
            playNewTrack(shuffleList.get(mCurrentTrackShuffle));
            return;
        }
        if (mSetting.getLoopMode() == LoopMode.ALL) {
            if (mCurrentTrackIndex == mTracks.size() - 1) {
                mCurrentTrackIndex = 0;
            } else {
                mCurrentTrackIndex++;
            }
            playNewTrack(mCurrentTrackIndex);
            return;
        }
        if (mSetting.getLoopMode() == LoopMode.ONE) {
            mMediaPlayer.seekTo(0);
            return;
        }
        if (mCurrentTrackIndex >= mTracks.size() - 1) {
            return;
        } else {
            mCurrentTrackIndex++;
            playNewTrack(mCurrentTrackIndex);
        }
    }

    /**
     * invoked when user click previous
     */
    public void handlePrevious() {
        if (mTracks == null || mMediaPlayer == null || mTracks.size() == 0) {
            return;
        }
        if (mSetting.getShuffleMode() == ShuffleMode.ON) {
            if (mCurrentTrackShuffle <= 0) {
                if (mSetting.getLoopMode() != LoopMode.ALL) {
                    return;
                }
                createShuffleList(mTracks.size());
                mCurrentTrackShuffle = mTracks.size();
            }
            if (mCurrentTrackShuffle > 0) {
                mCurrentTrackShuffle--;
            }
            playNewTrack(shuffleList.get(mCurrentTrackShuffle));
            return;
        }
        if (mSetting.getLoopMode() == LoopMode.ALL) {
            if (mCurrentTrackIndex == 0) {
                mCurrentTrackIndex = mTracks.size() - 1;
            } else {
                mCurrentTrackIndex--;
            }
            playNewTrack(mCurrentTrackIndex);
            return;
        }
        if (mSetting.getLoopMode() == LoopMode.ONE) {
            mMediaPlayer.seekTo(0);
            return;
        }
        if (mCurrentTrackIndex <= 0) {
            return;
        } else {
            mCurrentTrackIndex--;
            playNewTrack(mCurrentTrackIndex);
        }
    }

    /**
     * play requested track in specific position
     *
     * @param position
     */
    public void handleNewTrack(int position, boolean isReplay) {
        if (mCurrentTrackIndex == position && !isReplay) {
            return;
        }
        if (position < 0 || position >= mTracks.size()) {
            return;
        }
        playNewTrack(position);
    }

    /**
     * force replay if the track is playing
     *
     * @param tracks
     * @param position
     */
    public void handleNewTrack(List<Track> tracks, int position, boolean isReplay) {
        setTracks(tracks);
        if (isReplay) {
            playNewTrack(position);
            return;
        }
        if (mPlayingTrack == null || tracks.get(position).getId() != mPlayingTrack.getId()) {
            playNewTrack(position);
            return;
        }
        if (tracks.get(position).getId() == mPlayingTrack.getId()) {
            mCurrentTrackIndex = position;
        }
    }

    /**
     * force replay if the track is playing
     *
     * @param tracks
     * @param track
     */
    public void handleNewTrack(List<Track> tracks, Track track, boolean isReplay) {
        handleNewTrack(tracks, tracks.indexOf(track), isReplay);
    }

    /**
     * play requested track
     *
     * @param track
     */
    public void handleNewTrack(Track track, boolean isReplay) {
        handleNewTrack(mTracks.indexOf(track), isReplay);
    }

    private void playNewTrack(int position) {
        if (mTracks == null || mMediaPlayer == null || mTracks.size() == 0) {
            return;
        }
        mCurrentTrackIndex = position;
        mMediaPlayer.pause();
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        mMediaPlayer = new MediaPlayer();
        try {
            mPlayingTrack = mTracks.get(mCurrentTrackIndex);
            mMediaPlayer.setDataSource(mPlayingTrack.getSteamUrl());
            mPlayState = PlayState.PREPARING;
            notifyStateChanged();
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(MusicService.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyTrackChanged();
    }

    public void removeTrack(Track track) {
        if (track.getId() == mPlayingTrack.getId()) {
            return;
        }
        for (Track t : mTracks) {
            if (t.getId() == track.getId()) {
                mTracks.remove(track);
                for (int i = 0; i < mTracks.size(); i++) {
                    if (mTracks.get(i).getId() == mPlayingTrack.getId()) {
                        mCurrentTrackIndex = i;
                        break;
                    }
                }
                notifyTracksChanged();
                break;
            }
        }
    }

    public void handleSeek(int position) {
        if (mTracks == null || mMediaPlayer == null || mTracks.size() == 0) {
            return;
        }
        int seekToValue = (int) (position * mDuration / 100);
        mMediaPlayer.seekTo(seekToValue);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
        mPlayState = PlayState.PLAYING;
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                handleNext();
            }
        });
        notifyStateChanged();
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

    /**
     * you can register many time if you want, but just one instance
     * was stored in observers
     *
     * @param observer that interested in music service's information
     */
    @Override
    public void register(MusicServiceObserver observer) {
        observer.updateFirstTime(mSetting.getLoopMode(),
                mSetting.getShuffleMode(),
                mProgress,
                mDuration,
                mTracks.size() == 0 ? null : mTracks.get(mCurrentTrackIndex),
                mTracks,
                mPlayState);
        if (mMusicServiceObservers.contains(observer)) {
            return;
        }
        mMusicServiceObservers.add(observer);
    }

    @Override
    public void unregister(MusicServiceObserver observer) {
        mMusicServiceObservers.remove(observer);
    }

    @Override
    public void notifyLoopModeChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateLoopMode(mSetting.getLoopMode());
            }
        }
    }

    @Override
    public void notifyShuffleModeChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateShuffleMode(mSetting.getShuffleMode());
            }
        }
    }

    @Override
    public void notifyProgressChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateProgress(mProgress, mDuration);
            }
        }
    }

    @Override
    public void notifyTrackChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateTrack(mTracks.get(mCurrentTrackIndex));
            }
        }
    }

    @Override
    public void notifyStateChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateState(mPlayState);
            }
        }
    }

    @Override
    public void notifyTracksChanged() {
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateTracks(mTracks);
            }
        }
    }

}
