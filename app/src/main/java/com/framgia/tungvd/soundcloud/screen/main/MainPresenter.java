package com.framgia.tungvd.soundcloud.screen.main;

import android.os.Bundle;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.category.CategoryFragment;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void createPlayScreen() {

    }

    @Override
    public void backPressed() {

    }

}
