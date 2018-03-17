package com.framgia.tungvd.soundcloud.data.source.remote;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.Genre;
import com.framgia.tungvd.soundcloud.data.source.TracksDataSource;

public class TracksRemoteDataSource implements TracksDataSource {

    private static TracksRemoteDataSource sInstance;

    public static TracksRemoteDataSource getInstance() {
        if (sInstance == null) {
            synchronized (TracksRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new TracksRemoteDataSource();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getTracks(@NonNull LoadTracksCallback callback) {
        //not required in remote data source
    }

    @Override
    public void getTracksByGenre(@Genre String genre, int page,
                                 @NonNull LoadTracksCallback callback) {
        new GetDataAsyncTask(genre, page, callback).execute("");
    }

    @Override
    public void getTracksByGenre(@Genre String genre, int page, int limit,
                                 @NonNull LoadTracksCallback callback) {
        new GetDataAsyncTask(genre, page, limit, callback).execute("");
    }

    @Override
    public void deleteTrack(@NonNull long trackId) {
        //not required in remote data source
    }

    @Override
    public void saveTrack(@NonNull Track track) {
        //not required in remote data source
    }

}
