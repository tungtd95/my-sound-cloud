package com.framgia.tungvd.soundcloud.screen.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.CategoryAdapter;
import com.framgia.tungvd.soundcloud.custom.ItemDecoration;
import com.framgia.tungvd.soundcloud.custom.RecyclerItemClickListener;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;
import com.framgia.tungvd.soundcloud.screen.category.CategoryActivity;
import com.framgia.tungvd.soundcloud.screen.play.PlayFragment;

import java.util.List;

public class MainActivity extends BaseActivity
        implements MainContract.View, RecyclerItemClickListener.OnItemClickListener {

    private static final int GRID_COLUMN_NUMB = 3;
    private static final int GRID_SPACE = 20;

    private RecyclerView mRecyclerViewCategories;
    private CategoryAdapter mCategoryAdapter;

    private MainContract.Presenter mMainPresenter;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        showPlayScreen();

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);
        mMainPresenter.onStart();

        initMusicService();
    }

    private void initMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    private void initRecyclerView() {
        mRecyclerViewCategories = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this, GRID_COLUMN_NUMB);
        ItemDecoration itemDecoration = new ItemDecoration(GRID_SPACE, GRID_COLUMN_NUMB);
        mCategoryAdapter = new CategoryAdapter();
        mRecyclerViewCategories.setAdapter(mCategoryAdapter);
        mRecyclerViewCategories.setLayoutManager(layoutManager);
        mRecyclerViewCategories.addItemDecoration(itemDecoration);
        mRecyclerViewCategories.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerViewCategories, this));
    }

    private void onMusicServiceCreated() {
        // TODO: 03/09/18 service is created, MusicService instance is not null, start using here
    }

    @Override
    public void showCategories(List<Category> categories) {
        mCategoryAdapter.setCategories(categories);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(CategoryActivity.EXTRA_CATEGORY,
                mCategoryAdapter.getCategories().get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
