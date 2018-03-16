package com.framgia.tungvd.soundcloud.screen.download;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.model.MyDownloadManager;

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

    public static DownloadBottomSheetFragment newInstance(Track track) {
        DownloadBottomSheetFragment fragment = new DownloadBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DownloadBottomSheetFragment.ARGUMENT_TRACK, track);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        mTextViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permissionOK()) {
                    goOn();
                } else {
                    requestPermission();
                }
            }
        });
        if (!mTrack.isDownloadable()) {
            mTextViewDownload.setTextColor(getActivity()
                    .getResources()
                    .getColor(R.color.color_gray));
            mTextViewDownload.setClickable(false);
        }
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

    public void requestPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    public boolean permissionOK() {
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        if (permissionOK()) {
            goOn();
        }
    }

    private void goOn() {
        MyDownloadManager.getInstance(getActivity()).download(mTrack);
    }
}
