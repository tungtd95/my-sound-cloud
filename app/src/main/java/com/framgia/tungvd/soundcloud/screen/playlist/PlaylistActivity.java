package com.framgia.tungvd.soundcloud.screen.playlist;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.CreatePlaylistDialog;
import com.framgia.tungvd.soundcloud.custom.PlaylistAdapter;
import com.framgia.tungvd.soundcloud.custom.PlaylistClickListener;
import com.framgia.tungvd.soundcloud.data.model.Playlist;
import com.framgia.tungvd.soundcloud.data.source.PlaylistDataSource;
import com.framgia.tungvd.soundcloud.data.source.PlaylistRepository;
import com.framgia.tungvd.soundcloud.data.source.local.MyDBHelper;
import com.framgia.tungvd.soundcloud.data.source.local.PlaylistLocalDataSource;
import com.framgia.tungvd.soundcloud.data.source.remote.PlaylistRemoteDataSource;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;
import com.framgia.tungvd.soundcloud.util.AppExecutors;

import java.util.List;

public class PlaylistActivity extends BaseActivity
        implements PlaylistContract.View, PlaylistClickListener{

    private PlaylistContract.Presenter mPresenter;
    private RecyclerView mRecyclerPlaylist;
    private PlaylistAdapter mPlaylistAdapter;
    private PlaylistRepository mPlaylistRepository;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlsit);
        setTitle(R.string.title_playlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHandler = new Handler();
        mPlaylistRepository = PlaylistRepository.getInstance(
                PlaylistLocalDataSource.getInstance(
                        new AppExecutors(), MyDBHelper.getInstance(this)),
                PlaylistRemoteDataSource.getInstance());
        initBaseView();
        mRecyclerPlaylist = findViewById(R.id.recycler_playlist);
        mPlaylistAdapter = new PlaylistAdapter(this, true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerPlaylist.addItemDecoration(decoration);
        mRecyclerPlaylist.setLayoutManager(layoutManager);
        mRecyclerPlaylist.setAdapter(mPlaylistAdapter);
        mPresenter = new PlaylistPresenter();
        mPresenter.setView(this);
        mPresenter.onStart();
        initMusicService();
        updateView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                new CreatePlaylistDialog(this, mPlaylistRepository,
                        new PlaylistDataSource.PlaylistInsertCallback() {
                    @Override
                    public void onSuccess() {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                updateView();
                            }
                        };
                        mHandler.post(runnable);
                    }

                    @Override
                    public void onFail() {

                    }
                }).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void updateView() {
        mPlaylistRepository.getPlaylist(new PlaylistDataSource.PlaylistCallback() {
            @Override
            public void onPlaylistLoaded(final List<Playlist> playlists) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        mPlaylistAdapter.setPlaylists(playlists);
                    }
                };
                mHandler.post(r);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onItemDeleteClicked(int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
