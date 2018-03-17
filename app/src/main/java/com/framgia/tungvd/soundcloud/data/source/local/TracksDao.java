package com.framgia.tungvd.soundcloud.data.source.local;

import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.List;

public interface TracksDao {
    List<Track> getLocalTracks();

    List<Track> getDownloadedTracks();

    void insertTrack(Track track);

    int deleteTrackById(long trackId);

    void deleteTracks();
}
