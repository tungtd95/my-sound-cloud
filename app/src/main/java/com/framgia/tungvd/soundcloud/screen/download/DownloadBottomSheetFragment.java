package com.framgia.tungvd.soundcloud.screen.download;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;

public class DownloadBottomSheetFragment extends BottomSheetDialogFragment
        implements DownloadContract.View {

    public static final String ARGUMENT_TRACK = "ARGUMENT_TRACK";

    private DownloadContract.Presenter mPresenter;
    private Track mTrack;

    private TextView mTextViewTrack;
    private TextView mTextViewUser;
    private TextView mTextViewPlay;
    private TextView mTextViewDownload;
    private TextView mTextViewDelete;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mTrack = getArguments().getParcelable(ARGUMENT_TRACK);
        mTextViewTrack.setText(mTrack.getTitle());
        mTextViewUser.setText(mTrack.getUserName());
    }

    void initView(View view) {
        mTextViewTrack = view.findViewById(R.id.text_track);
        mTextViewUser = view.findViewById(R.id.text_user);
        mTextViewPlay = view.findViewById(R.id.text_play);
        mTextViewDownload = view.findViewById(R.id.text_download);
        mTextViewDelete = view.findViewById(R.id.text_delete);
    }

    @Override
    public void displayDownloadProgress(int progress) {

    }
}
