package com.framgia.tungvd.soundcloud.data.source;

import android.support.annotation.StringDef;

import static com.framgia.tungvd.soundcloud.data.source.Genre.ALL_AUDIO;
import static com.framgia.tungvd.soundcloud.data.source.Genre.ALL_MUSIC;
import static com.framgia.tungvd.soundcloud.data.source.Genre.ALTERNATIVEROCK;
import static com.framgia.tungvd.soundcloud.data.source.Genre.AMBIENT;
import static com.framgia.tungvd.soundcloud.data.source.Genre.CLASSICAL;
import static com.framgia.tungvd.soundcloud.data.source.Genre.COUNTRY;

@StringDef({ALL_MUSIC, ALL_AUDIO, ALTERNATIVEROCK, AMBIENT, CLASSICAL, COUNTRY})
public @interface Genre {
    String ALL_MUSIC = "all-music";
    String ALL_AUDIO = "all-audio";
    String ALTERNATIVEROCK = "alternativerock";
    String AMBIENT = "ambient";
    String CLASSICAL = "classical";
    String COUNTRY = "country";
}
