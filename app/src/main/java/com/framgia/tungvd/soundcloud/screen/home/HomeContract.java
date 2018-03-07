package com.framgia.tungvd.soundcloud.screen.home;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.BasePresenter;

import java.util.List;

public interface HomeContract {

    interface View {
        void showCategories(List<Category> categories);
    }

    interface Presenter extends BasePresenter<View> {
        void getCategories();
        void openCategory();
        void filterCategories(String keyWord);
    }
}
