package com.framgia.tungvd.soundcloud.data.source.local;

import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.List;

public interface TracksDao {
    /**
     * Select all tracks from the tracks table
     *
     * @return all tracks
     */
    List<Track> getTracks();

    /**
     * insert a downloaded track into database
     *
     * @param track tobe insert
     */
    void insertTrack(Track track);

    /**
     * delete existed track by id
     *
     * @param trackId
     * @return the number of tracks deleted. should always be 1.
     */
    int deleteTrackById(long trackId);

    /**
     * delete all tracks in the database
     */
    void deleteTracks();
}
