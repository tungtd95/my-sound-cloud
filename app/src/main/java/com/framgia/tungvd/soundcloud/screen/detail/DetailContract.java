package com.framgia.tungvd.soundcloud.screen.detail;

import com.framgia.tungvd.soundcloud.screen.BasePresenter;

public interface DetailContract {
    interface View {
        void displayDownloadProgress(int progress);
    }

    interface Presenter extends BasePresenter<View> {
        void download();
    }
}
