package com.framgia.tungvd.soundcloud.screen.main;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.BasePresenter;
import com.framgia.tungvd.soundcloud.screen.category.CategoryFragment;

public interface MainContract {
    interface View {
        void showPlayScreen();

        void showHome();
    }

    interface Presenter extends BasePresenter<View> {
        void createPlayScreen();

        void backPressed();
    }
}
