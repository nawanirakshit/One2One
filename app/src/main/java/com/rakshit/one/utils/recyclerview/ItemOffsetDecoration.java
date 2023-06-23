package com.rakshit.one.utils.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.papers.utils.extension.CommonKt;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private final int mItemHorizontalOffset;
    private final int mItemVerticalOffset;

    private ItemOffsetDecoration(int itemOffsetHorizontal, int itemOfsetVertical) {
        mItemHorizontalOffset = itemOffsetHorizontal;
        mItemVerticalOffset =  itemOfsetVertical;
    }

    public ItemOffsetDecoration(@NonNull Context context,  float itemOffsetIdHorizontal, float itemOffsetIdVertical) {
        this(CommonKt.dpToPx(itemOffsetIdHorizontal), CommonKt.dpToPx(itemOffsetIdVertical));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemHorizontalOffset, mItemVerticalOffset, mItemHorizontalOffset, mItemVerticalOffset);
    }
}