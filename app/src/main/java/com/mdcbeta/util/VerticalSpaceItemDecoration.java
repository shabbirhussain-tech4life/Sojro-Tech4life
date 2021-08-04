package com.mdcbeta.util;

import android.graphics.Rect;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shakil Karim on 3/12/17.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(float verticalSpaceHeight) {
        this.verticalSpaceHeight = (int)Math.round(verticalSpaceHeight);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
