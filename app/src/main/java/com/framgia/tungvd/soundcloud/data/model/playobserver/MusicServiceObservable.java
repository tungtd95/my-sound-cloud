package com.framgia.tungvd.soundcloud.data.model.playobserver;

public interface MusicServiceObservable {
    void register(MusicServiceObserver observer);
    void notifyLoopModeChanged();
    void notifyShuffleModeChanged();
    void notifyProgressChanged();
    void notifyTrackChanged();
    void notifyTracksChanged();
}
