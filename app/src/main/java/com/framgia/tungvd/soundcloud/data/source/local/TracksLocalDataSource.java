package com.framgia.tungvd.soundcloud.data.source.local;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.TracksDataSource;
import com.framgia.tungvd.soundcloud.util.AppExecutors;

import java.util.List;

public class TracksLocalDataSource implements TracksDataSource {

    private static volatile TracksLocalDataSource sInstance;
    private TracksDao mTracksDao;
    private AppExecutors mAppExecutors;

    private TracksLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull TracksDao tracksDao) {
        mTracksDao = tracksDao;
        mAppExecutors = appExecutors;
    }

    public static TracksLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    TracksDao tracksDao) {
        if (sInstance == null) {
            synchronized (TracksLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new TracksLocalDataSource(appExecutors, tracksDao);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getTracks(@NonNull final LoadTracksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Track> tracks = mTracksDao.getTracks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tracks.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTracksLoaded(tracks);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTracksByGenre(String genre, int page, @NonNull LoadTracksCallback callback) {
        //not required for the local data
    }

    @Override
    public void deleteTrack(@NonNull final long trackId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTracksDao.deleteTrackById(trackId);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveTrack(@NonNull final Track track) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTracksDao.insertTrack(track);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }
}
