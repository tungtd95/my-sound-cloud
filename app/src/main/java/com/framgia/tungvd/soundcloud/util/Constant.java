package com.framgia.tungvd.soundcloud.util;

public class Constant {

    private Constant() {
    }

    public class SoundCloud {

        private SoundCloud() {
        }

        public static final String SOUND_CLOUD_API =
                "https://api.soundcloud.com/tracks?client_id=%s&genre=soundcloud%sgenres%s%s&page=%d";
        public static final String PARAM_CLIENT = "?client_id=";
        public static final String PARAM_LIMIT = "&limit=";
        public static final String PARAM_DOT = "%3A";
        public static final String EXTENSION = ".mp4";
        public static final String NULL_VALUE = "null";
    }

    public class JsonProperties {

        private JsonProperties() {
        }

        public static final String KIND = "kind";
        public static final String ID = "id";
        public static final String CREATED_AT = "created_at";
        public static final String DURATION = "duration";
        public static final String STATE = "state";
        public static final String TAG_LIST = "tag_list";
        public static final String DOWNLOADABLE = "downloadable";
        public static final String GENRE = "genre";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String LABEL_NAME = "label_name";
        public static final String STREAM_URL = "stream_url";
        public static final String USER_ID = "id";
        public static final String USER_NAME = "username";
        public static final String AVATAR_URL = "avatar_url";
        public static final String USER = "user";
        public static final String DOWNLOAD_URL = "download_url";
        public static final String ARTWORK_URL = "artwork_url";
    }

}
