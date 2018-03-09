package com.framgia.tungvd.soundcloud.screen.main;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.BasePresenter;

import java.util.List;

public interface MainContract {
    interface View {
        void showCategories(List<Category> categories);
    }

    interface Presenter extends BasePresenter<View> {
        void getCategories();
        void filterCategories(String keyWord);
    }
}
