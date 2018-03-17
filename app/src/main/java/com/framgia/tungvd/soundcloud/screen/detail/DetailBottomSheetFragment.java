package com.framgia.tungvd.soundcloud.screen.detail;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.DownloadState;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.model.MyDownloadManager;
import com.framgia.tungvd.soundcloud.data.model.downloadobserver.DownloadObserver;

import java.util.List;

public class DetailBottomSheetFragment extends BottomSheetDialogFragment
        implements DetailContract.View, DownloadObserver {

    public static final String ARGUMENT_TRACK = "ARGUMENT_TRACK";
    private static final int REQUEST_PERMISSION = 1;

    private DetailContract.Presenter mPresenter;
    private Track mTrack;
    private MyDownloadManager mMyDownloadManager;

    private TextView mTextViewTrack;
    private TextView mTextViewUser;
    private TextView mTextViewPlay;
    private TextView mTextViewDownload;
    private TextView mTextViewDelete;
    private ImageView mImageDownload;
    private Handler mHandler;

    public static DetailBottomSheetFragment newInstance(Track track) {
        DetailBottomSheetFragment fragment = new DetailBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailBottomSheetFragment.ARGUMENT_TRACK, track);
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
        mMyDownloadManager = MyDownloadManager.getInstance(getContext());
        mMyDownloadManager.register(this);
        mTrack = getArguments().getParcelable(ARGUMENT_TRACK);
        mTextViewTrack.setText(mTrack.getTitle());
        mTextViewUser.setText(mTrack.getUserName());
        mTextViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted()) {
                    download();
                } else {
                    requestPermission();
                }
            }
        });
        mHandler = new Handler();
        updateState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMyDownloadManager.unregister(this);
    }

    void updateState() {
        switch (mMyDownloadManager.getDownloadState(mTrack)) {
            case DownloadState.DOWNLOADING:
                mTextViewDownload.setTextColor(getActivity()
                        .getResources()
                        .getColor(R.color.color_black));
                mTextViewDownload.setClickable(false);
                mImageDownload.setBackgroundResource(R.drawable.download_animation);
                AnimationDrawable animation = (AnimationDrawable) mImageDownload.getBackground();
                animation.start();
                break;
            case DownloadState.DOWNLOADED:
                mTextViewDownload.setTextColor(getActivity()
                        .getResources()
                        .getColor(R.color.color_black));
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_done);
                mTextViewDownload.setClickable(false);
                break;
            case DownloadState.DOWNLOADABLE:
                mTextViewDownload.setTextColor(getActivity()
                        .getResources()
                        .getColor(R.color.color_black));
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_download);
                break;
            case DownloadState.UN_DOWNLOADABLE:
                mTextViewDownload.setTextColor(getActivity()
                        .getResources()
                        .getColor(R.color.color_gray));
                mTextViewDownload.setClickable(false);
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_off);
                break;
            default:
                mTextViewDownload.setTextColor(getActivity()
                        .getResources()
                        .getColor(R.color.color_gray));
                mTextViewDownload.setClickable(false);
                mImageDownload.setBackgroundResource(R.drawable.ic_cloud_off);
                break;
        }
    }

    void initView(View view) {
        mTextViewTrack = view.findViewById(R.id.text_track);
        mTextViewUser = view.findViewById(R.id.text_user);
        mTextViewPlay = view.findViewById(R.id.text_play);
        mTextViewDownload = view.findViewById(R.id.text_download);
        mTextViewDelete = view.findViewById(R.id.text_delete);
        mImageDownload = view.findViewById(R.id.image_download);
    }

    @Override
    public void displayDownloadProgress(int progress) {

    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);
    }

    public boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        if (isPermissionGranted()) {
            download();
        }
    }

    private void download() {
        MyDownloadManager.getInstance(getActivity()).download(mTrack);
    }

    @Override
    public void updateDownloadState() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateState();
            }
        };
        mHandler.post(runnable);
    }

    @Override
    public void updateDownloadingTracks(List<Track> tracks) {
        //no need to implement
    }

    @Override
    public void updateDownloadedTracks(List<Track> tracks) {
        updateDownloadState();
    }

    @Override
    public void updateFirstTime(List<Track> tracksDownloaded, List<Track> tracksDownloading) {
        //no need to implement
    }
}
