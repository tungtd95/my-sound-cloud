package com.framgia.tungvd.soundcloud.data.source.remote;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.framgia.tungvd.soundcloud.BuildConfig;
import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.Genre;
import com.framgia.tungvd.soundcloud.data.source.TracksDataSource.LoadTracksCallback;
import com.framgia.tungvd.soundcloud.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetDataAsyncTask extends AsyncTask<String, Void, Void> {

    private static final String TAG = "GetDataAsyncTask";

    private LoadTracksCallback mCallback;
    private ArrayList<Track> mTracks;
    private String mGenre;
    private int mPage;

    public GetDataAsyncTask(@Genre String genre, int page, @NonNull LoadTracksCallback callback) {
        mCallback = callback;
        mPage = page;
        mGenre = genre;
    }

    @Override
    protected Void doInBackground(@Genre String... strings) {



        String url = String.format(
                Config.SOUND_CLOUD_API,
                BuildConfig.SOUND_CLOUD_KEY,
                "%3A",
                "%3A",
                mGenre,
                mPage);
        try {
            String data = getJSONStringFromURL(url);
            mTracks = getTracksFromJson(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mTracks == null || mTracks.isEmpty()) {
            mCallback.onDataNotAvailable();
        } else {
            mCallback.onTracksLoaded(mTracks);
        }
    }

    private String getJSONStringFromURL(String urlString) throws IOException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();

        urlConnection.disconnect();
        return jsonString;
    }

    private ArrayList<Track> getTracksFromJson(String jsonData) {
        ArrayList<Track> tracks = new ArrayList<>();
        // TODO: 03/05/18 parse json data to array list of tracks

        return tracks;
    }
}
