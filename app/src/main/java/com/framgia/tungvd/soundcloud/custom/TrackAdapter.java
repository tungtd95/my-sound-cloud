package com.framgia.tungvd.soundcloud.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder>{

    private List<Track> mTracks;

    public TrackAdapter() {
        mTracks = new ArrayList<>();
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
        notifyDataSetChanged();
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        holder.bindView(mTracks.get(position));
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTrackName;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mTextViewTrackName = itemView.findViewById(R.id.text_view_track_name);
        }

        public void bindView(Track track) {
            if (track == null) {
                return;
            }
            mTextViewTrackName.setText(track.getTitle());
        }
    }
}
