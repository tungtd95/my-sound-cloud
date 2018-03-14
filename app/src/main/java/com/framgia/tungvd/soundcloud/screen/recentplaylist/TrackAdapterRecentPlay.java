package com.framgia.tungvd.soundcloud.screen.recentplaylist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapterRecentPlay extends RecyclerView.Adapter<TrackAdapterRecentPlay.TrackViewHolder> {

    private List<Track> mTracks;
    private Track mTrack;

    public TrackAdapterRecentPlay() {
        mTracks = new ArrayList<>();
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
        notifyDataSetChanged();
    }

    public void setTrack(Track track) {
        mTrack = track;
        notifyDataSetChanged();
    }

    @Override
    public TrackAdapterRecentPlay.TrackViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new TrackAdapterRecentPlay.TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackAdapterRecentPlay.TrackViewHolder holder, int position) {
        holder.bindView(mTracks.get(position));
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTrack;
        private ImageView mImageViewTrack;
        private TextView mTextViewArtist;
        private ImageView mImageViewAction;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mTextViewTrack = itemView.findViewById(R.id.text_item_track);
            mTextViewArtist = itemView.findViewById(R.id.text_item_artist);
            mImageViewTrack = itemView.findViewById(R.id.image_item_track);
            mImageViewAction = itemView.findViewById(R.id.image_item_action);
        }

        public void bindView(Track track) {
            if (track == null) {
                return;
            }
            mTextViewTrack.setText(track.getTitle());
            mTextViewArtist.setText(track.getUserName());
        }
    }
}
