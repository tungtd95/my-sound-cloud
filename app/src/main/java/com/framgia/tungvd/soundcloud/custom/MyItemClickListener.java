package com.framgia.tungvd.soundcloud.custom;

import com.framgia.tungvd.soundcloud.data.model.Track;

public interface MyItemClickListener {
    void onItemClicked(int position);
    void onItemDetail(Track track);
}
