package com.framgia.tungvd.soundcloud.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx;
    private int mGridSize;

    private boolean mNeedLeftSpacing;

    public ItemDecoration(int gridSpacingPx, int gridSize) {
        mSizeGridSpacingPx = gridSpacingPx;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        int frameWidth = (int)
                ((parent.getWidth() - (float) mSizeGridSpacingPx * (mGridSize - 1)) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition =
                ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition < mGridSize) {
            outRect.top = padding;
        } else {
            outRect.top = mSizeGridSpacingPx;
        }
        outRect.bottom = 0;
        if (itemPosition % mGridSize == 0) {
            outRect.left = padding;
            outRect.right = padding;
            mNeedLeftSpacing = true;
            return;
        }
        if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.right = padding;
            outRect.left = padding;
            return;
        }
        if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx - padding;
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx - padding;
            } else {
                outRect.right = mSizeGridSpacingPx / 2;
            }
            return;
        }
        if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx / 2;
            outRect.right = mSizeGridSpacingPx - padding;
            return;
        }

        mNeedLeftSpacing = false;
        outRect.left = mSizeGridSpacingPx / 2;
        outRect.right = mSizeGridSpacingPx / 2;
    }
}