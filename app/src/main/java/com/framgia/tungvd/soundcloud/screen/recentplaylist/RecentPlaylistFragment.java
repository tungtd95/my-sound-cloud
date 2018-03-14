package com.framgia.tungvd.soundcloud.screen.recentplaylist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.RecyclerItemClickListener;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;

import java.util.ArrayList;

public class RecentPlaylistFragment extends BaseFragment
        implements RecentPlaylistContract.View, RecyclerItemClickListener.OnItemClickListener {

    private RecentPlaylistContract.Presenter mPresenter;
    private RecyclerView mRecyclerViewPlaylist;
    private TrackAdapterRecentPlay mAdapterRecentPlay;
    private MusicService mMusicService;

    public void setMusicService(MusicService musicService) {
        mMusicService = musicService;
        musicService.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerViewPlaylist = view.findViewById(R.id.recycler_recent_playlist);
        mAdapterRecentPlay = new TrackAdapterRecentPlay();
        mRecyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewPlaylist.setAdapter(mAdapterRecentPlay);
        mRecyclerViewPlaylist.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                mRecyclerViewPlaylist,
                this)
        );
        mPresenter = new RecentPlaylistPresenter();
        mPresenter.setView(this);
        mPresenter.onStart();
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

    }

    @Override
    public void updateTracks(ArrayList<Track> tracks) {
        mAdapterRecentPlay.setTracks(tracks);
    }

    @Override
    public void updateState(int playState) {

    }

    @Override
    public void updateFirstTime(int loopMode,
                                int shuffleMode,
                                long progress,
                                long duration,
                                @Nullable Track track,
                                ArrayList<Track> tracks,
                                int playState) {
        updateTracks(tracks);
        updateTrack(track);
        updateState(playState);
    }

    @Override
    public void onItemClick(View view, int position) {
        mMusicService.handleNewTrack(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
