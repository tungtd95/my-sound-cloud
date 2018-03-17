package com.framgia.tungvd.soundcloud.screen.recenttrack;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.DownloadState;
import com.framgia.tungvd.soundcloud.data.model.MyDownloadManager;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.model.downloadobserver.DownloadObserver;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class RecentTrackFragment extends BaseFragment
        implements RecentTrackContract.View, DownloadObserver {

    private RecentTrackContract.Presenter mPresenter;
    private ImageView mImageTrack;
    private ImageView mImageDownload;
    private MyDownloadManager mMyDownloadManager;
    private Track mTrack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mPresenter = new RecentTrackPresenter();
        mPresenter.setView(this);
        mPresenter.onStart();
        mMyDownloadManager = MyDownloadManager.getInstance(getActivity());
        mMyDownloadManager.register(this);
        updateView();
        mImageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyDownloadManager.download(mTrack);
            }
        });
    }

    void initView(View view) {
        mImageDownload = view.findViewById(R.id.image_download);
        mImageTrack = view.findViewById(R.id.image_track_sub);
    }

    void updateView() {
        if (mTrack == null) {
            return;
        }
        mImageDownload.setClickable(false);
        switch (mMyDownloadManager.getDownloadState(mTrack)) {
            case DownloadState.DOWNLOADABLE:
                mImageDownload.setClickable(true);
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_download);
                break;
            case DownloadState.DOWNLOADED:
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_done);
                break;
            case DownloadState.DOWNLOADING:
                mImageDownload.setBackgroundResource(R.drawable.download_animation);
                AnimationDrawable animation = (AnimationDrawable) mImageDownload.getBackground();
                animation.start();
                break;
            case DownloadState.UN_DOWNLOADABLE:
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_off);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMyDownloadManager.unregister(this);
    }

    @Override
    public void updateLoopMode(int loopMode) {
        // no need to implement
    }

    @Override
    public void updateShuffleMode(int shuffleMode) {
        // no need to implement
    }

    @Override
    public void updateProgress(long progress, long duration) {
        // no need to implement
    }

    @Override
    public void updateTrack(@Nullable Track track) {
        mTrack = track;
        Transformation transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();
        Picasso.get().load(track.getArtworkUrl()).fit()
                .transform(transformation).centerInside()
                .into(mImageTrack, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                mImageTrack.setImageResource(R.drawable.music_icon_origin);
            }
        });
        updateView();
    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {
        // no need to implement
    }

    @Override
    public void updateState(int playState) {
        // TODO: 03/13/18 add some animation here
    }

    @Override
    public void updateFirstTime(int loopMode,
                                int shuffleMode,
                                long progress,
                                long duration,
                                @Nullable Track track,
                                ArrayList<Track> tracks,
                                int playState) {
        updateTrack(track);
        updateState(playState);
    }

    @Override
    public void updateDownloadState() {
        updateView();
    }

    @Override
    public void updateDownloadingTracks(List<Track> tracks) {
        //no need to implement
    }

    @Override
    public void updateDownloadedTracks(List<Track> tracks) {
        //no need to implement
    }

    @Override
    public void updateFirstTime(List<Track> tracksDownloaded, List<Track> tracksDownloading) {
        //no need to implement
    }
}
