package com.example.eric.quickheadline.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int mSpaceTop;
    private int mSpaceBottom;
    private int mSpaceLeft;
    private int mSpaceRight;

    public VerticalSpaceItemDecoration(int mSpaceTop, int mSpaceBottom, int mSpaceLeft, int mSpaceRight) {
        this.mSpaceTop = mSpaceTop;
        this.mSpaceBottom = mSpaceBottom;
        this.mSpaceLeft = mSpaceLeft;
        this.mSpaceRight = mSpaceRight;
    }

    public VerticalSpaceItemDecoration() {
        this(0,8,0,0);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = mSpaceTop;
        outRect.bottom = mSpaceBottom;
        outRect.left = mSpaceLeft;
        outRect.right = mSpaceRight;
    }
}
