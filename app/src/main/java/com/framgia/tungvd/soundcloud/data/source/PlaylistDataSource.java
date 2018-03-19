package com.framgia.tungvd.soundcloud.data.source;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Playlist;
import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.List;

public interface PlaylistDataSource {
    interface LoadPlaylistCallback {
        void onPlaylistLoaded(List<Playlist> playlists);

        void onDataNotAvailable();
    }

    interface PlaylistCallback {
        void onSuccess();

        void onFail();
    }

    void getPlaylist(@NonNull LoadPlaylistCallback callback);

    void savePlaylist(@NonNull Playlist playlist, @NonNull PlaylistCallback callback);

    void deleteList(@NonNull Playlist playlist, @NonNull PlaylistCallback callback);

    void addTrackToPlaylist(@NonNull Track track, @NonNull Playlist playlist,
                            @NonNull PlaylistCallback callback);

    void removeTrackFromPlaylist(@NonNull Track track, @NonNull Playlist playlist,
                                 @NonNull PlaylistCallback callback);
}
