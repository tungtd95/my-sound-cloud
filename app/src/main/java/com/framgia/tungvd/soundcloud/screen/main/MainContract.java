package com.framgia.tungvd.soundcloud.screen.main;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.BasePresenter;
import com.framgia.tungvd.soundcloud.screen.category.CategoryFragment;

import java.util.List;

public interface MainContract {
    interface View {
        void showPlayScreen();
        void showCategories(List<Category> categories);
    }

    interface Presenter extends BasePresenter<View> {
        void getCategories();
        void filterCategories(String keyWord);
    }
}
