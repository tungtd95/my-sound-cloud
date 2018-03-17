package com.framgia.tungvd.soundcloud.data.source;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.List;

public interface TracksDataSource {

    interface LoadTracksCallback {
        void onTracksLoaded(List<Track> tracks);

        void onDataNotAvailable();
    }

    void getTracks(@NonNull LoadTracksCallback callback);

    void getTracksByGenre(@Genre String genre, int page,
                          @NonNull LoadTracksCallback callback);

    void getTracksByGenre(@Genre String genre, int page, int limit,
                          @NonNull LoadTracksCallback callback);

    void deleteTrack(@NonNull long trackId);

    void saveTrack(@NonNull Track track);
}
