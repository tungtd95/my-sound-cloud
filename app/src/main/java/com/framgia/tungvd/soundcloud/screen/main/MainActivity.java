package com.framgia.tungvd.soundcloud.screen.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;
import com.framgia.tungvd.soundcloud.screen.category.CategoryFragment;
import com.framgia.tungvd.soundcloud.screen.home.HomeFragment;

public class MainActivity extends BaseActivity
        implements MainContract.View, ShowCategoryListener{

    private MainContract.Presenter mMainPresenter;

    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_main);

        mHomeFragment = new HomeFragment();
        mHomeFragment.setShowCategoryListener(this);

        replaceFragment(mHomeFragment);

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);
        mMainPresenter.onStart();
    }

    public void showCategoryScreen(Category category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CategoryFragment.EXTRA_GENRE, category.getGenre());
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void showPlayScreen() {

    }

    @Override
    public void showHome() {
        replaceFragment(mHomeFragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout_replace, fragment);
        ft.commit();
    }

    @Override
    public void onCategoryClicked(Category category) {
        showCategoryScreen(category);
    }
}
