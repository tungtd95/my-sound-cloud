package com.framgia.tungvd.soundcloud.data.source;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Playlist;
import com.framgia.tungvd.soundcloud.data.model.Track;

public class PlaylistRepository implements PlaylistDataSource {
    @Override
    public void getPlaylist(@NonNull PlaylistCallback callback) {

    }

    @Override
    public void savePlaylist(@NonNull Playlist playlist) {

    }

    @Override
    public void deleteList(@NonNull Playlist playlist) {

    }

    @Override
    public void addTrackToPlaylist(@NonNull Track track, @NonNull Playlist playlist) {

    }

    @Override
    public void removeTrackFromPlaylist(@NonNull Track track, @NonNull Playlist playlist) {

    }
}
