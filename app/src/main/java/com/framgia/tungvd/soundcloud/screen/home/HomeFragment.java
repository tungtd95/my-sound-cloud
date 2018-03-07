package com.framgia.tungvd.soundcloud.screen.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.CategoryAdapter;
import com.framgia.tungvd.soundcloud.custom.ItemDecoration;
import com.framgia.tungvd.soundcloud.custom.RecyclerItemClickListener;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.source.CategoriesRepository;
import com.framgia.tungvd.soundcloud.screen.BaseFragment;
import com.framgia.tungvd.soundcloud.screen.main.ShowCategoryListener;

import java.util.List;

public class HomeFragment extends BaseFragment
        implements HomeContract.View, RecyclerItemClickListener.OnItemClickListener {

    private static final int GRID_COLUMN_NUMB = 2;
    private static final int GRID_SPACE = 25;

    private RecyclerView mRecyclerViewCategories;
    private CategoryAdapter mCategoryAdapter;
    private ShowCategoryListener mShowCategoryListener;

    private HomeContract.Presenter mPresenter;

    public void setShowCategoryListener(ShowCategoryListener showCategoryListener) {
        mShowCategoryListener = showCategoryListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerViewCategories = view.findViewById(R.id.recycler_view_categories);
        mCategoryAdapter = new CategoryAdapter();
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getActivity(), GRID_COLUMN_NUMB);
        ItemDecoration decoration = new ItemDecoration(GRID_SPACE, GRID_COLUMN_NUMB);

        mRecyclerViewCategories.setLayoutManager(layoutManager);
        mRecyclerViewCategories.addItemDecoration(decoration);
        mRecyclerViewCategories.setAdapter(mCategoryAdapter);
        mRecyclerViewCategories.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                mRecyclerViewCategories,
                this));

        mPresenter = new HomePresenter(CategoriesRepository.getInstance());
        mPresenter.setView(this);
        mPresenter.onStart();
    }

    @Override
    public void showCategories(List<Category> categories) {
        mCategoryAdapter.setCategories(categories);
    }

    public void showCategory(Category category) {
        if (mShowCategoryListener != null) {
            mShowCategoryListener.onCategoryClicked(category);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        showCategory(mCategoryAdapter.getCategories().get(position));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
