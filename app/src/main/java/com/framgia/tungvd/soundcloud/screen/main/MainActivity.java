package com.framgia.tungvd.soundcloud.screen.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.custom.CategoryAdapter;
import com.framgia.tungvd.soundcloud.custom.ItemDecoration;
import com.framgia.tungvd.soundcloud.custom.RecyclerItemClickListener;
import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.model.MusicService;
import com.framgia.tungvd.soundcloud.screen.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements MainContract.View, RecyclerItemClickListener.OnItemClickListener{

    private static final int WAIT_SERVICE_TIME = 10; /* millisecond */
    private static final int GRID_COLUMN_NUMB = 2;
    private static final int GRID_SPACE = 25;

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

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);
        mMainPresenter.onStart();

        initMusicService();
    }

    private void initMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void initRecyclerView() {
        mRecyclerViewCategories = findViewById(R.id.recycler_view_categories);
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

    }

    @Override
    public void showPlayScreen() {

    }

    @Override
    public void showCategories(List<Category> categories) {
        mCategoryAdapter.setCategories(categories);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
