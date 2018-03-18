package com.framgia.tungvd.soundcloud.custom;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framgia.tungvd.soundcloud.R;
import com.framgia.tungvd.soundcloud.data.model.Track;
import com.framgia.tungvd.soundcloud.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter
        extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private List<Track> mTracks;
    private Track mTrack;
    private TrackClickListener mItemClickListener;
    private boolean isDownloading;

    public void setItemClickListener(TrackClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public TrackAdapter() {
        mTracks = new ArrayList<>();
    }

    public TrackAdapter(boolean isDownloading) {
        mTracks = new ArrayList<>();
        this.isDownloading = isDownloading;
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
    public TrackAdapter.TrackViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new TrackAdapter.TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackAdapter.TrackViewHolder holder, int position) {
        holder.bindView(mTracks.get(position), position);
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
        private RelativeLayout mRelativeItem;

        public TrackViewHolder(View itemView) {
            super(itemView);
            mTextViewTrack = itemView.findViewById(R.id.text_item_track);
            mTextViewArtist = itemView.findViewById(R.id.text_item_artist);
            mImageViewTrack = itemView.findViewById(R.id.image_item_track);
            mImageViewAction = itemView.findViewById(R.id.image_item_action);
            mRelativeItem = itemView.findViewById(R.id.relative_track_item);
        }

        public void bindView(final Track track, final int position) {
            if (track == null) {
                return;
            }
            mTextViewTrack.setText(track.getTitle());
            mTextViewArtist.setText(track.getUserName());
            mImageViewAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemDetail(track);
                    }
                }
            });
            mRelativeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClicked(position);
                    }
                }
            });
            if (!track.getArtworkUrl().equals(Constant.SoundCloud.NULL_VALUE)) {
                Picasso.get().load(track.getArtworkUrl())
                        .fit().centerInside().into(mImageViewTrack);
            } else {
                mImageViewTrack.setImageResource(R.drawable.ic_music);
            }

            if (isDownloading) {
                mImageViewAction.setClickable(false);
                mImageViewAction.setBackgroundResource(R.drawable.download_animation);
                AnimationDrawable animation = (AnimationDrawable) mImageViewAction.getBackground();
                animation.start();
            }
        }
    }
}
