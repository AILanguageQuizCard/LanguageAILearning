package com.example.chatgpt.adapter.task;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;

public class TaskRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int currentPosition = parent.getChildAdapterPosition(view);
        if (currentPosition == 0) {
            // 每排最左侧的边距
            outRect.left = ConvertUtils.dp2px(15);
            outRect.top = ConvertUtils.dp2px(10);
            outRect.right = ConvertUtils.dp2px(15);
            outRect.bottom = ConvertUtils.dp2px(0);
        } else {
            // 普通元素的边距都是 5
            outRect.left = ConvertUtils.dp2px(15);
            outRect.top = ConvertUtils.dp2px(10);
            outRect.right = ConvertUtils.dp2px(15);
            outRect.bottom = ConvertUtils.dp2px(0);
        }
    }
}