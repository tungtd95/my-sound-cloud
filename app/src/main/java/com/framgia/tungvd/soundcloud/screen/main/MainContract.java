package com.framgia.tungvd.soundcloud.screen.main;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.BasePresenter;

public interface MainContract {
    interface View {
        void showCategoryScreen(Category category);

        void showPlayScreen();

        void showHome();
    }

    interface Presenter extends BasePresenter<View> {
        void createPlayScreen();

        void backPressed();
    }
}
