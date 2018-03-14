package com.framgia.tungvd.soundcloud.screen.download;

import com.framgia.tungvd.soundcloud.screen.BasePresenter;

public interface DownloadContract {
    interface View {
        void displayDownloadProgress(int progress);
    }

    interface Presenter extends BasePresenter<View> {
        void download();
    }
}
