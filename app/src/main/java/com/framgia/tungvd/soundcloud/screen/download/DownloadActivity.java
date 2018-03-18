package com.framgia.tungvd.soundcloud.screen.download;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.adapter.TrackAdapter;
import com.framgia.tungvd.soundcloud.data.model.MyDownloadManager;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;

import java.util.List;

public class DownloadActivity extends BaseActivity implements DownloadContract.View {

    private RecyclerView mRecyclerDownloaded;
    private RecyclerView mRecyclerDownloading;
    private TrackAdapter mAdapterDownloaded;
    private TrackAdapter mAdapterDownloading;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        setTitle(R.string.title_download);
        mHandler = new Handler();
        initView();
        initMusicService();
        MyDownloadManager.getInstance(this).register(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyDownloadManager.getInstance(this).unregister(this);
    }

    void initView() {
        initBaseView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerDownloaded = findViewById(R.id.recycler_downloaded);
        mRecyclerDownloading = findViewById(R.id.recycler_playlist);
        mAdapterDownloaded = new TrackAdapter();
        mAdapterDownloading = new TrackAdapter(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        mRecyclerDownloading.setLayoutManager(layoutManager);
        mRecyclerDownloaded.setLayoutManager(layoutManager1);
        mRecyclerDownloaded.setAdapter(mAdapterDownloaded);
        mRecyclerDownloading.setAdapter(mAdapterDownloading);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void updateDownloadState() {
        //no need to implement
    }

    @Override
    public void updateDownloadingTracks(List<Track> tracks) {
        mAdapterDownloading.setTracks(tracks);
    }

    @Override
    public void updateDownloadedTracks(final List<Track> tracks) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mAdapterDownloaded.setTracks(tracks);
            }
        };
        mHandler.post(runnable);
    }

    @Override
    public void updateFirstTime(List<Track> tracksDownloaded, List<Track> tracksDownloading) {
        updateDownloadedTracks(tracksDownloaded);
        updateDownloadingTracks(tracksDownloading);
    }
}
