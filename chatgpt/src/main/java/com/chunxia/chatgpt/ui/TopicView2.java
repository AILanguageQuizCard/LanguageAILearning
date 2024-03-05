package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.chunxia.chatgpt.R;

public class TopicView2 extends RelativeLayout {

    public TopicView2(Context context) {
        this(context, null);
    }

    public TopicView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.topic_view, this, true);
    }


}