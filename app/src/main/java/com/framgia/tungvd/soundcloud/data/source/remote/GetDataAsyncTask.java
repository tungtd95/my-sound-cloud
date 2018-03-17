package com.framgia.tungvd.soundcloud.data.source.remote;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.framgia.tungvd.soundcloud.BuildConfig;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.data.source.Genre;
import com.framgia.tungvd.soundcloud.data.source.TracksDataSource.LoadTracksCallback;
import com.framgia.tungvd.soundcloud.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetDataAsyncTask extends AsyncTask<String, Void, Void> {

    private static final int READ_TIMEOUT = 10000; /*millisecond*/
    private static final int CONNECT_TIMEOUT = 15000; /*millisecond*/
    private static final int LIMIT_DEFAULT = -1;

    private LoadTracksCallback mCallback;
    private ArrayList<Track> mTracks;
    private String mGenre;
    private int mPage;
    private int mLimit = LIMIT_DEFAULT;

    public GetDataAsyncTask(@Genre String genre, int page,
                            @NonNull LoadTracksCallback callback) {
        mCallback = callback;
        mPage = page;
        mGenre = genre;
    }

    public GetDataAsyncTask(@Genre String genre, int page, int limit,
                            @NonNull LoadTracksCallback callback) {
        mCallback = callback;
        mPage = page;
        mGenre = genre;
        mLimit = limit;
    }

    @Override
    protected Void doInBackground(@Genre String... strings) {

        String url = String.format(
                Constant.SoundCloud.SOUND_CLOUD_API,
                BuildConfig.SOUND_CLOUD_KEY,
                Constant.SoundCloud.PARAM_DOT,
                Constant.SoundCloud.PARAM_DOT,
                mGenre,
                mPage);
        if (mLimit != LIMIT_DEFAULT) {
            url = new StringBuilder(url)
                    .append(Constant.SoundCloud.PARAM_LIMIT)
                    .append(mLimit)
                    .toString();
        }
        try {
            String data = getJSONStringFromURL(url);
            mTracks = getTracksFromJson(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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

        HttpURLConnection urlConnection;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECT_TIMEOUT);

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

    private ArrayList<Track> getTracksFromJson(String jsonData) throws JSONException {
        ArrayList<Track> tracks = new ArrayList<>();
        JSONArray jsa = new JSONArray(jsonData);
        for (int i = 0; i < jsa.length(); i++) {
            JSONObject jso = new JSONObject(jsa.getString(i));

            String kind = jso.getString(Constant.JsonProperties.KIND);
            long id = jso.getLong(Constant.JsonProperties.ID);
            String createAt = jso.getString(Constant.JsonProperties.CREATED_AT);
            long duration = jso.getLong(Constant.JsonProperties.DURATION);
            String state = jso.getString(Constant.JsonProperties.STATE);
            String tagList = jso.getString(Constant.JsonProperties.TAG_LIST);
            boolean downloadable = jso.getBoolean(Constant.JsonProperties.DOWNLOADABLE);
            String genre = jso.getString(Constant.JsonProperties.GENRE);
            String title = jso.getString(Constant.JsonProperties.TITLE);
            String description = jso.getString(Constant.JsonProperties.DESCRIPTION);
            String labelName = jso.getString(Constant.JsonProperties.LABEL_NAME);
            String streamUrl = jso.getString(Constant.JsonProperties.STREAM_URL);
            String downloadUrl = "";
            String artworkUrl = jso.getString(Constant.JsonProperties.ARTWORK_URL);
            if (downloadable) {
                downloadUrl = jso.getString(Constant.JsonProperties.DOWNLOAD_URL);
            }

            JSONObject userJso = jso.getJSONObject(Constant.JsonProperties.USER);

            long userId = userJso.getLong(Constant.JsonProperties.USER_ID);
            String userName = userJso.getString(Constant.JsonProperties.USER_NAME);
            String avatarUrl = userJso.getString(Constant.JsonProperties.AVATAR_URL);

            Track track = new Track.Builder()
                    .kind(kind)
                    .id(id)
                    .createAt(createAt)
                    .duration(duration)
                    .state(state)
                    .tagList(tagList)
                    .downloadable(downloadable)
                    .genre(genre)
                    .title(title)
                    .description(description)
                    .labelName(labelName)
                    .streamUrl(streamUrl)
                    .user(userId, userName, avatarUrl)
                    .downloadUrl(downloadUrl)
                    .artworkUrl(artworkUrl)
                    .build();

            tracks.add(track);
        }
        return tracks;
    }
}
