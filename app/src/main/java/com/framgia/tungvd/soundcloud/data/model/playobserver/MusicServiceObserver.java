package com.framgia.tungvd.soundcloud.data.model.playobserver;

import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.model.PlayState;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;

import java.util.ArrayList;

public interface MusicServiceObserver {
    void updateLoopMode(@LoopMode int loopMode);
    void updateShuffleMode(@ShuffleMode int shuffleMode);
    void updateProgress(long progress, long duration);
    void updateTrack(Track track);
    void updateTracks(ArrayList<Track> tracks);
    void updateState(@PlayState int playState);
}
