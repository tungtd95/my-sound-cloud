package com.framgia.tungvd.soundcloud.data.source;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Track;

public class TracksRepository implements TracksDataSource {

    private static TracksRepository sInstance;

    private final TracksDataSource mTracksRemoteDataSource;
    private final TracksDataSource mTracksLocalDataSource;

    private TracksRepository(@NonNull TracksDataSource tracksRemoteDataSource,
                            @NonNull TracksDataSource tracksLocalDataSource) {
        mTracksRemoteDataSource = tracksRemoteDataSource;
        mTracksLocalDataSource = tracksLocalDataSource;
    }

    public static TracksRepository getInstance(@NonNull TracksDataSource tracksRemoteDataSource,
                                               @NonNull TracksDataSource tracksLocalDataSource) {
        if (sInstance == null) {
            sInstance = new TracksRepository(tracksRemoteDataSource, tracksLocalDataSource);
        }
        return sInstance;
    }

    @Override
    public void getTracks(@NonNull LoadTracksCallback callback) {
        mTracksLocalDataSource.getTracks(callback);
    }

    @Override
    public void getTracksByGenre(String genre, int page,
                                 @NonNull LoadTracksCallback callback) {
        mTracksRemoteDataSource.getTracksByGenre(genre, page, callback);
    }

    @Override
    public void getTracksByGenre(String genre, int page, int limit,
                                 @NonNull LoadTracksCallback callback) {
        mTracksRemoteDataSource.getTracksByGenre(genre, page, limit, callback);
    }

    @Override
    public void deleteTrack(@NonNull long trackId) {
        mTracksLocalDataSource.deleteTrack(trackId);
    }

    @Override
    public void saveTrack(@NonNull Track track) {
        mTracksLocalDataSource.saveTrack(track);
    }
}
