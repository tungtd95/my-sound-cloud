package com.framgia.tungvd.soundcloud.data.model;

import android.graphics.Bitmap;

import com.framgia.tungvd.soundcloud.BuildConfig;
import com.framgia.tungvd.soundcloud.util.Constant;

public class Track {
    private String mKind;
    private long mId;
    private String mCreateAt;
    private long mDuration;
    private String mState;
    private String mTagList;
    private boolean mDownloadable;
    private String mGenre;
    private String mTitle;
    private String mDescription;
    private String mLabelName;
    private String mSteamUrl;
    private long mUserId;
    private String mUserName;
    private String mAvatarUrl;
    private Bitmap mImageBitMap;

    private Track(Builder builder) {
        mKind = builder.mKind;
        mId = builder.mId;
        mCreateAt = builder.mCreateAt;
        mDuration = builder.mDuration;
        mState = builder.mState;
        mTagList = builder.mTagList;
        mDownloadable = builder.mDownloadable;
        mGenre = builder.mGenre;
        mTitle = builder.mTitle;
        mDescription = builder.mDescription;
        mLabelName = builder.mLabelName;
        mSteamUrl = builder.mStreamUrl;
        mUserId = builder.mUserId;
        mUserName = builder.mUserName;
        mAvatarUrl = builder.mAvatarUrl;
    }

    public static class Builder {
        private String mKind = "";
        private long mId = -1;
        private String mCreateAt = "";
        private long mDuration = -1;
        private String mState = "";
        private String mTagList = "";
        private boolean mDownloadable = false;
        private String mGenre = "";
        private String mTitle = "";
        private String mDescription = "";
        private String mLabelName = "";
        private String mStreamUrl = "";
        private long mUserId = -1;
        private String mUserName = "";
        private String mAvatarUrl = "";

        public Builder kind(String kind) {
            mKind = kind;
            return this;
        }

        public Builder id(long id) {
            mId = id;
            return this;
        }

        public Builder createAt(String createAt) {
            mCreateAt = createAt;
            return this;
        }

        public Builder duration(long duration) {
            mDuration = duration;
            return this;
        }

        public Builder state(String state) {
            mState = state;
            return this;
        }

        public Builder tagList(String tagList) {
            mTagList = tagList;
            return this;
        }

        public Builder downloadable(boolean downloadable) {
            mDownloadable = downloadable;
            return this;
        }

        public Builder genre(String genre) {
            mGenre = genre;
            return this;
        }

        public Builder title(String title) {
            mTitle = title.trim();
            return this;
        }

        public Builder description(String description) {
            mDescription = description;
            return this;
        }

        public Builder labelName(String labelName) {
            mLabelName = labelName;
            return this;
        }

        public Builder streamUrl(String streamUrl) {
            mStreamUrl = new StringBuilder(streamUrl)
                    .append(Constant.SoundCloud.PARAM_CLIENT)
                    .append(BuildConfig.SOUND_CLOUD_KEY)
                    .toString();
            return this;
        }

        public Builder user(long userId, String userName, String avatarUrl) {
            mUserId = userId;
            mUserName = userName.trim();
            mAvatarUrl = avatarUrl;
            return this;
        }

        public Track build() {
            return new Track(this);
        }
    }

    public String getKind() {
        return mKind;
    }

    public long getId() {
        return mId;
    }

    public String getCreateAt() {
        return mCreateAt;
    }

    public long getDuration() {
        return mDuration;
    }

    public String getState() {
        return mState;
    }

    public String getTagList() {
        return mTagList;
    }

    public boolean isDownloadable() {
        return mDownloadable;
    }

    public String getGenre() {
        return mGenre;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLabelName() {
        return mLabelName;
    }

    public String getSteamUrl() {
        return mSteamUrl;
    }

    public long getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public Bitmap getImageBitMap() {
        return mImageBitMap;
    }

    public void setImageBitMap(Bitmap imageBitMap) {
        mImageBitMap = imageBitMap;
    }
}
