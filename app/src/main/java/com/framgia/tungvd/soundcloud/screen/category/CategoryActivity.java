package com.framgia.tungvd.soundcloud.screen.category;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.RecyclerItemClickListener;
import com.framgia.tungvd.soundcloud.custom.TrackAdapter;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.TracksRepository;
import com.framgia.tungvd.soundcloud.data.source.local.TracksLocalDataSource;
import com.framgia.tungvd.soundcloud.data.source.remote.TracksRemoteDataSource;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;
import com.framgia.tungvd.soundcloud.screen.play.PlayFragment;
import com.framgia.tungvd.soundcloud.util.AppExecutors;

import java.util.List;

public class CategoryActivity extends BaseActivity
        implements CategoryContract.View, RecyclerItemClickListener.OnItemClickListener{

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
                new StringBuilder(getString(R.string.loading))
                        .append(" ")
                        .append(mCategory.getName())
                        .toString());
        mRecyclerViewItems = findViewById(R.id.recycler_view_items);

        mTrackAdapter = new TrackAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewItems.setLayoutManager(layoutManager);
        mRecyclerViewItems.setAdapter(mTrackAdapter);
        mRecyclerViewItems.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerViewItems, this));

        mPresenter = new CategoryPresenter(
                TracksRepository.getInstance(TracksRemoteDataSource.getInstance(),
                        TracksLocalDataSource.getInstance(new AppExecutors(),
                                null)));
        mPresenter.setView(this);
        mPresenter.setCategory((Category) getIntent().getExtras().getParcelable(EXTRA_CATEGORY));
        mPresenter.onStart();
        mRelativeSubController = findViewById(R.id.relative_sub_controller);
        mRelativeSubController.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mMusicService != null) {
            mMusicService.setTracks(mTrackAdapter.getTracks());
            mMusicService.handleNewTrack(position);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

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
}
