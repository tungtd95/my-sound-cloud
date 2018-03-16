package com.framgia.tungvd.soundcloud.data.model.downloadobserver;

import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.List;

public interface DownloadObserver {
    void updateDownloadingTracks(List<Track> tracks);
    void updateFirstTime(List<Track> tracks);
}
