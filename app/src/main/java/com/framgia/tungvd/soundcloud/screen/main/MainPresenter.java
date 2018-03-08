package com.framgia.tungvd.soundcloud.screen.main;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.source.CategoriesDataSource;
import com.framgia.tungvd.soundcloud.data.source.CategoriesRepository;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private CategoriesRepository mCategoriesRepository;

    public MainPresenter() {
        mCategoriesRepository = CategoriesRepository.getInstance();
    }

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {
        getCategories();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void getCategories() {
        mCategoriesRepository.getCategories(new CategoriesDataSource.LoadCategoriesCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                mView.showCategories(categories);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void filterCategories(String keyWord) {

    }
}
