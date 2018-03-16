package com.framgia.tungvd.soundcloud.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.tungvd.soundcloud.BuildConfig;
import com.framgia.tungvd.soundcloud.util.Constant;

public class Track implements Parcelable {
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
    private String mDownloadUrl;
    private String mArtworkUrl;

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
        mDownloadUrl = builder.mDownloadUrl;
        mArtworkUrl = builder.mArtworkUrl;
    }

    protected Track(Parcel in) {
        mKind = in.readString();
        mId = in.readLong();
        mCreateAt = in.readString();
        mDuration = in.readLong();
        mState = in.readString();
        mTagList = in.readString();
        mDownloadable = in.readByte() != 0;
        mGenre = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mLabelName = in.readString();
        mSteamUrl = in.readString();
        mUserId = in.readLong();
        mUserName = in.readString();
        mAvatarUrl = in.readString();
        mImageBitMap = in.readParcelable(Bitmap.class.getClassLoader());
        mDownloadUrl = in.readString();
        mArtworkUrl = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mKind);
        parcel.writeLong(mId);
        parcel.writeString(mCreateAt);
        parcel.writeLong(mDuration);
        parcel.writeString(mState);
        parcel.writeString(mTagList);
        parcel.writeByte((byte) (mDownloadable ? 1 : 0));
        parcel.writeString(mGenre);
        parcel.writeString(mTitle);
        parcel.writeString(mDescription);
        parcel.writeString(mLabelName);
        parcel.writeString(mSteamUrl);
        parcel.writeLong(mUserId);
        parcel.writeString(mUserName);
        parcel.writeString(mAvatarUrl);
        parcel.writeParcelable(mImageBitMap, i);
        parcel.writeString(mDownloadUrl);
        parcel.writeString(mArtworkUrl);
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
        private String mDownloadUrl = "";
        private String mArtworkUrl = "";

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

        public Builder downloadUrl(String url) {
            mDownloadUrl = new StringBuilder(url)
                    .append(Constant.SoundCloud.PARAM_CLIENT)
                    .append(BuildConfig.SOUND_CLOUD_KEY)
                    .toString();
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

        public Builder artworkUrl(String artworkUrl) {
            mArtworkUrl = artworkUrl;
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

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }
}
