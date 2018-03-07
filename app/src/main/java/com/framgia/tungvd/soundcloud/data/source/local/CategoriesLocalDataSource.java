package com.framgia.tungvd.soundcloud.data.source.local;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Category;
import com.framgia.tungvd.soundcloud.data.source.CategoriesDataSource;
import com.framgia.tungvd.soundcloud.data.source.Genre;

import java.util.ArrayList;
import java.util.List;


public class CategoriesLocalDataSource implements CategoriesDataSource {

    private static CategoriesLocalDataSource sInstance;

    private CategoriesLocalDataSource() {
    }

    public static CategoriesLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new CategoriesLocalDataSource();
        }
        return sInstance;
    }

    @Override
    public void getCategories(@NonNull LoadCategoriesCallback callback) {
        List<Category> mCategories = new ArrayList<>();
        Category allMusic = new Category(Genre.ALL_MUSIC, Category.ALL_MUSIC);
        Category allAudio = new Category(Genre.ALL_AUDIO, Category.ALL_AUDIO);
        Category alternativerock = new Category(Genre.ALTERNATIVEROCK, Category.ALTERNATIVEROCK);
        Category ambient = new Category(Genre.AMBIENT, Category.AMBIENT);
        Category classical = new Category(Genre.CLASSICAL, Category.CLASSICAL);
        Category country = new Category(Genre.COUNTRY, Category.COUNTRY);
        mCategories.add(allMusic);
        mCategories.add(allAudio);
        mCategories.add(alternativerock);
        mCategories.add(ambient);
        mCategories.add(classical);
        mCategories.add(country);
        callback.onCategoriesLoaded(mCategories);
    }
}
