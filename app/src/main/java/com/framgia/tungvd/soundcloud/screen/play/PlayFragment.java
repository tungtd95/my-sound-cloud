package com.framgia.tungvd.soundcloud.screen.play;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.model.PlayState;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;
import com.framgia.tungvd.soundcloud.util.UsefulFunc;

import java.util.ArrayList;

public class PlayFragment extends BottomSheetDialogFragment
        implements PlayContract.View, View.OnClickListener {

    private static final String TAG = "PlayFragment";
    private static final int PROGRESS_MAX = 100;

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
    private Handler mHandler;
    private MusicService mMusicService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onMusicServiceStarted(MusicService musicService) {
        mMusicService = musicService;
        mMusicService.register(this);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
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
        mPresenter = new PlayPresenter();
        mPresenter.setView(this);
        mPresenter.onStart();
    }

    private void initPlayingView(View view) {
        mButtonPlay = view.findViewById(R.id.button_play);
        mButtonNext = view.findViewById(R.id.button_next);
        mButtonPrevious = view.findViewById(R.id.button_previous);
        mButtonShuffle = view.findViewById(R.id.button_shuffle);
        mButtonLoop = view.findViewById(R.id.button_loop);
        mTextViewProgress = view.findViewById(R.id.text_view_progress);
        mTextViewDuration = view.findViewById(R.id.text_view_duration);
        mSeekBarMain = view.findViewById(R.id.seek_bar_main);
        mImageViewTrack = view.findViewById(R.id.image_view_track);
        mSeekBarMain.setMax(PROGRESS_MAX);
        mButtonLoop.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
    }

    @Override
    public void updateLoopMode(final int loopMode) {
        if (mButtonLoop == null) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
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
        };
        mHandler.post(runnable);
    }

    @Override
    public void updateShuffleMode(final int shuffleMode) {
        if (mButtonShuffle == null) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
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
        };
        mHandler.post(runnable);
    }

    @Override
    public void updateProgress(final long progress, final long duration) {
        if (mSeekBarMain == null || mTextViewDuration == null || mTextViewDuration == null) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mSeekBarMain.setProgress((int) (progress * PROGRESS_MAX / duration));
                mTextViewProgress.setText(UsefulFunc.convertProgressToTime(progress));
                mTextViewDuration.setText(UsefulFunc.convertProgressToTime(duration));
            }
        };
        mHandler.post(runnable);
    }

    @Override
    public void updateTrack(@Nullable Track track) {
        // TODO: 03/09/18 display recent track
    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {
        // TODO: 03/09/18 display recent tracks
    }

    @Override
    public void updateState(final int playState) {
        if (mButtonPlay == null) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (playState) {
                    case PlayState.PLAYING:
                        mButtonPlay.setBackgroundResource(R.drawable.ic_pause_circle_filled);
                        break;
                    case PlayState.PAUSED:
                        mButtonPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
                        break;
                    case PlayState.PREPARING:
                        mButtonPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
                        break;
                    default:
                        mButtonPlay.setBackgroundResource(R.drawable.ic_play_circle_filled);
                        break;
                }
            }
        };
        mHandler.post(runnable);
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
}
