package com.framgia.tungvd.soundcloud.screen.play;

import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.setting.LoopMode;
import com.framgia.tungvd.soundcloud.data.source.setting.PlayState;
import com.framgia.tungvd.soundcloud.data.source.setting.ShuffleMode;
import com.framgia.tungvd.soundcloud.screen.BasePresenter;

public interface PlayContract {
    interface View {
        void showPlayState(@PlayState int playState);

        void showShuffleMode(@ShuffleMode int shuffle);

        void showLoopMode(@LoopMode int loopMode);

        void showProgress(int progress);

        void showTrack(Track track);
    }

    interface Presenter extends BasePresenter<View> {
        void clickPlay();

        void clickNext();

        void clickShuffle();

        void clickRepeat();

        void seek();
    }
}
