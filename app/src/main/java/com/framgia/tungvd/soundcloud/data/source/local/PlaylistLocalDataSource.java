package com.framgia.tungvd.soundcloud.data.source.local;

import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.data.model.Playlist;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.PlaylistDataSource;
import com.framgia.tungvd.soundcloud.util.AppExecutors;


public class PlaylistLocalDataSource implements PlaylistDataSource {

    private static volatile PlaylistLocalDataSource sInstance;
    private PlaylistDao mPlaylistDao;
    private AppExecutors mAppExecutors;

    private PlaylistLocalDataSource(@NonNull AppExecutors appExecutors,
                                  @NonNull PlaylistDao playlistDao) {
        mPlaylistDao = playlistDao;
        mAppExecutors = appExecutors;
    }

    public static PlaylistLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                    PlaylistDao playlistDao) {
        if (sInstance == null) {
            synchronized (PlaylistLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new PlaylistLocalDataSource(appExecutors, playlistDao);
                }
            }
        }
        return sInstance;
    }

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
