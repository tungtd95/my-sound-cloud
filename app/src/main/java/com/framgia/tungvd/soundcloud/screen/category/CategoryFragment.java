package com.framgia.tungvd.soundcloud.screen.category;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.TrackAdapter;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.TracksRepository;
import com.framgia.tungvd.soundcloud.data.source.local.TracksLocalDataSource;
import com.framgia.tungvd.soundcloud.data.source.remote.TracksRemoteDataSource;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;
import com.framgia.tungvd.soundcloud.util.AppExecutors;

import java.util.List;

public class CategoryFragment extends BaseFragment implements CategoryContract.View {

    public static final String ARGUMENT_CATEGORY = "ARGUMENT_CATEGORY";

    private ProgressDialog mProgressDialog;
    private CategoryPresenter mPresenter;
    private RecyclerView mRecyclerViewItems;
    private TrackAdapter mTrackAdapter;
    private Category mCategory;

    public static CategoryFragment newInstance(Category category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CategoryFragment.ARGUMENT_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_item_by_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCategory = getArguments().getParcelable(ARGUMENT_CATEGORY);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(
                new StringBuilder(getString(R.string.loading))
                        .append(" ")
                        .append(mCategory.getName())
                        .toString());
        mRecyclerViewItems = view.findViewById(R.id.recycler_view_items);

        mTrackAdapter = new TrackAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewItems.setLayoutManager(layoutManager);
        mRecyclerViewItems.setAdapter(mTrackAdapter);

        mPresenter = new CategoryPresenter(
                TracksRepository.getInstance(TracksRemoteDataSource.getInstance(),
                        TracksLocalDataSource.getInstance(new AppExecutors(),
                                null)));
        mPresenter.setView(this);
        mPresenter.setCategory((Category) getArguments().getParcelable(ARGUMENT_CATEGORY));
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

}
