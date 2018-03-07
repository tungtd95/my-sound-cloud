package com.framgia.tungvd.soundcloud.screen.home;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.source.CategoriesDataSource;
import com.framgia.tungvd.soundcloud.data.source.CategoriesRepository;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;
    private ArrayList<Category> mCategories;
    CategoriesRepository mCategoriesRepository;

    public HomePresenter(CategoriesRepository categoriesRepository) {
        mCategoriesRepository = categoriesRepository;
    }

    @Override
    public void setViewMain(HomeContract.View viewMain) {
        mView = viewMain;
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
    public void openCategory() {

    }

    @Override
    public void filterCategories(String keyWord) {

    }
}
