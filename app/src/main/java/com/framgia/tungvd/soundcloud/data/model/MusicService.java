package com.framgia.tungvd.soundcloud.data.model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObservable;
import com.framgia.tungvd.soundcloud.data.model.playobserver.MusicServiceObserver;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.source.setting.Setting;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;
import com.framgia.tungvd.soundcloud.screen.MyNotification;
import com.framgia.tungvd.soundcloud.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MusicService extends Service
        implements MusicServiceObservable, MediaPlayer.OnPreparedListener {

    private static final int CHECK_MEDIA_DELAY = 100; /* time delay when check media's progress*/
    private static final int SERVICE_ID = 10;
    private static final String SEPARATE = "/";
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
    private MyNotification mMyNotification;

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
        getSetting();
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
        mMyNotification = new MyNotification(this);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyNotification.ACTION_NEXT)) {
                    handleNext();
                    return;
                }
                if (intent.getAction().equals(MyNotification.ACTION_PLAY)) {
                    changeMediaState();
                    return;
                }
                if (intent.getAction().equals(MyNotification.ACTION_PREVIOUS)) {
                    handlePrevious();
                    return;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyNotification.ACTION_NEXT);
        intentFilter.addAction(MyNotification.ACTION_PLAY);
        intentFilter.addAction(MyNotification.ACTION_PREVIOUS);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void saveSetting(Setting setting) {
        SharedPreferences preferences =
                getSharedPreferences(Constant.SharedConstant.PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constant.SharedConstant.PREF_LOOP_MODE, setting.getLoopMode());
        editor.putInt(Constant.SharedConstant.PREF_SHUFFLE_MODE, setting.getShuffleMode());
        editor.commit();
    }

    private void getSetting() {
        mSetting = new Setting(LoopMode.OFF, ShuffleMode.OFF);
        SharedPreferences preferences =
                getSharedPreferences(Constant.SharedConstant.PREF_FILE, MODE_PRIVATE);
        int loopMode = preferences.getInt(Constant.SharedConstant.PREF_LOOP_MODE, LoopMode.OFF);
        int shuffleMode = preferences.getInt(Constant.SharedConstant.PREF_SHUFFLE_MODE,
                ShuffleMode.OFF);
        if (shuffleMode == ShuffleMode.ON) {
            handleShuffle();
        }
        if (loopMode == LoopMode.ONE) {
            handleLoop();
            return;
        }
        if (loopMode == LoopMode.ALL) {
            handleLoop();
            handleLoop();
        }
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
        mPlayingTrack = mTracks.get(mCurrentTrackIndex);
        if (mPlayingTrack.getLocalPath().isEmpty()) {
            try {
                mMediaPlayer.setDataSource(mPlayingTrack.getSteamUrl());
                mPlayState = PlayState.PREPARING;
                notifyStateChanged();
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(MusicService.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String path = new StringBuilder(baseDir)
                    .append(mPlayingTrack.getLocalPath()).append(SEPARATE)
                    .append(mPlayingTrack.getId()).append(Constant.SoundCloud.EXTENSION).toString();
            try {
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mPlayState = PlayState.PLAYING;
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        handleNext();
                    }
                });
                notifyStateChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
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
        saveSetting(mSetting);
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateLoopMode(mSetting.getLoopMode());
            }
        }
    }

    @Override
    public void notifyShuffleModeChanged() {
        saveSetting(mSetting);
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

    private void displayNotification() {
        if (mPlayingTrack == null) {
            return;
        }
        startForeground(SERVICE_ID, mMyNotification.getNotification(mPlayState, mPlayingTrack));
        if (mPlayState != PlayState.PLAYING) {
            stopForeground(false);
        }
    }

    @Override
    public void notifyTrackChanged() {
        displayNotification();
        for (MusicServiceObserver observer : mMusicServiceObservers) {
            if (observer != null) {
                observer.updateTrack(mTracks.get(mCurrentTrackIndex));
            }
        }
    }

    @Override
    public void notifyStateChanged() {
        displayNotification();
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
