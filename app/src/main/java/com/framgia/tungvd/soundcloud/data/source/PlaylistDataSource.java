package com.framgia.tungvd.soundcloud.data.source;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Playlist;
import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.List;

public interface PlaylistDataSource {
    interface PlaylistCallback {
        void onPlaylistLoaded(List<Playlist> playlists);

        void onDataNotAvailable();
    }

    void getPlaylist(@NonNull PlaylistCallback callback);
    void savePlaylist(@NonNull Playlist playlist);
    void deleteList(@NonNull Playlist playlist);
    void addTrackToPlaylist(@NonNull Track track,@NonNull Playlist playlist);
    void removeTrackFromPlaylist(@NonNull Track track,@NonNull Playlist playlist);
}
