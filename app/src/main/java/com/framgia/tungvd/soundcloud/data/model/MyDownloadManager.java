package com.framgia.tungvd.soundcloud.data.model;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.framgia.tungvd.soundcloud.data.model.downloadobserver.DownloadObservable;
import com.framgia.tungvd.soundcloud.data.model.downloadobserver.DownloadObserver;
import com.framgia.tungvd.soundcloud.util.Constant;

import java.io.File;
import java.util.ArrayList;

public class MyDownloadManager implements DownloadObservable {

    private static MyDownloadManager sInstance;
    private Context mContext;
    private ArrayList<Track> mTracksDownloading;
    private ArrayList<Long> mIdTrackDownloading;
    private ArrayList<Track> mTracksDownloaded;
    private ArrayList<DownloadObserver> mObservers;

    private MyDownloadManager(Context context) {
        mContext = context;
        mTracksDownloading = new ArrayList<>();
        mIdTrackDownloading = new ArrayList<>();
        mTracksDownloaded = new ArrayList<>();
        mObservers = new ArrayList<>();
    }

    public static MyDownloadManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MyDownloadManager(context);
        }
        return sInstance;
    }

    public void download(Track track) {
        if (!track.isDownloadable()) {
            return;
        }
        String fileName = new StringBuilder()
                .append(track.getId())
                .append(Constant.SoundCloud.EXTENSION)
                .toString();

        Uri uri = Uri.parse(track.getDownloadUrl());
        File file = mContext.getFilesDir();
        file.mkdir();
        DownloadManager downloadManager = (DownloadManager)
                mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false)
                .setTitle(track.getTitle())
                .setDestinationInExternalPublicDir(file.getAbsolutePath(), fileName));
        mContext.registerReceiver(onDownload,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        mTracksDownloading.add(track);
        mIdTrackDownloading.add(id);
        notifyDownloadStateChanged();
        notifyDownloadingTracksChanged();
    }

    private BroadcastReceiver onDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
            for (int i = 0; i < mIdTrackDownloading.size(); i++) {
                if (mIdTrackDownloading.get(i) == id) {
                    mTracksDownloaded.add(mTracksDownloading.get(i));
                    mIdTrackDownloading.remove(i);
                    mTracksDownloading.remove(i);
                    notifyDownloadStateChanged();
                    notifyDownloadingTracksChanged();
                    notifyDownloadedTracksChanged();
                    break;
                }
            }
        }
    };

    public @DownloadState int getDownloadState(Track track) {
        if (!track.isDownloadable()) {
            return DownloadState.UN_DOWNLOADABLE;
        }
        for (Track t : mTracksDownloading) {
            if (t.getId() == track.getId()) {
                return DownloadState.DOWNLOADING;
            }
        }
        for (Track t : mTracksDownloaded) {
            if (t.getId() == track.getId()) {
                return DownloadState.DOWNLOADED;
            }
        }
        return DownloadState.DOWNLOADABLE;
    }

    @Override
    public void register(DownloadObserver observer) {
        if (mObservers.contains(observer)) {
            return;
        }
        mObservers.add(observer);
        observer.updateFirstTime(mTracksDownloaded, mTracksDownloading);
    }

    @Override
    public void unregister(DownloadObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyDownloadStateChanged() {
        for (DownloadObserver o : mObservers) {
            o.updateDownloadState();
        }
    }

    @Override
    public void notifyDownloadingTracksChanged() {
        for (DownloadObserver o : mObservers) {
            o.updateDownloadingTracks(mTracksDownloading);
        }
    }

    @Override
    public void notifyDownloadedTracksChanged() {
        for (DownloadObserver o : mObservers) {
            o.updateDownloadedTracks(mTracksDownloaded);
        }
    }
}
