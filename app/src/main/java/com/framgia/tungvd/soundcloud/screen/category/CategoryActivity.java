package com.framgia.tungvd.soundcloud.screen.category;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.MyItemClickListener;
import com.framgia.tungvd.soundcloud.custom.TrackAdapter;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.TracksRepository;
import com.framgia.tungvd.soundcloud.data.source.local.TracksLocalDataSource;
import com.framgia.tungvd.soundcloud.data.source.remote.TracksRemoteDataSource;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;
import com.framgia.tungvd.soundcloud.screen.download.DownloadBottomSheetFragment;
import com.framgia.tungvd.soundcloud.util.AppExecutors;

import java.util.List;

public class CategoryActivity extends BaseActivity
        implements CategoryContract.View, MyItemClickListener{

    public static final String EXTRA_CATEGORY =
            "com.framgia.tungvd.soundcloud.screen.category.extras.EXTRA_CATEGORY";

    private ProgressDialog mProgressDialog;
    private CategoryPresenter mPresenter;
    private RecyclerView mRecyclerViewItems;
    private TrackAdapter mTrackAdapter;
    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mCategory = getIntent().getExtras().getParcelable(EXTRA_CATEGORY);
        setTitle(mCategory.getName());
        initView();
        initMusicService();
    }

    private void initView() {
        initBaseView();
        mMusicService = MusicService.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(
                new StringBuilder(getString(R.string.msg_loading))
                        .append(" ")
                        .append(mCategory.getName())
                        .toString());
        mRecyclerViewItems = findViewById(R.id.recycler_view_items);

        mTrackAdapter = new TrackAdapter();
        mTrackAdapter.setItemClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewItems.setLayoutManager(layoutManager);
        mRecyclerViewItems.setAdapter(mTrackAdapter);
        mPresenter = new CategoryPresenter(
                TracksRepository.getInstance(TracksRemoteDataSource.getInstance(),
                        TracksLocalDataSource.getInstance(new AppExecutors(),
                                null)));
        mPresenter.setView(this);
        mPresenter.setCategory((Category) getIntent().getExtras().getParcelable(EXTRA_CATEGORY));
        mPresenter.onStart();
    }

    @Override
    public void changeLoadingIndicatorState(Boolean isLoading) {
        if (isLoading) {
            mProgressDialog.show();
            return;
        }
        mProgressDialog.dismiss();
    }

    @Override
    public void showTracks(List<Track> tracks) {
        mTrackAdapter.setTracks(tracks);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClicked(int position) {
        if (mMusicService != null) {
            mMusicService.setTracks(mTrackAdapter.getTracks());
            mMusicService.handleNewTrack(position);
        }
    }

    @Override
    public void onItemDetail(Track track) {
        mPresenter.download(track);
        DownloadBottomSheetFragment fragment = new DownloadBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DownloadBottomSheetFragment.ARGUMENT_TRACK, track);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }
}
