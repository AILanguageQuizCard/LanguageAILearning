package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunxia.chatgpt.R;

public class SubscriptionOptionView extends RelativeLayout {

    private ImageView chooseView;

    private TextView titleView;

    private TextView priceView;

    private TextView savedView;

    private RelativeLayout root;

    public SubscriptionOptionView(Context context) {
        this(context, null);
    }

    public SubscriptionOptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscriptionOptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        root = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.subscription_option_view, this, true);
        chooseView = root.findViewById(R.id.choose_imageview);
    }

    public void choose() {
        chooseView.setImageResource(R.drawable.subscription_option_choosed);
        root.setBackgroundResource(R.drawable.subscription_option_bg);
    }

    public void unchoose() {
        chooseView.setImageResource(R.drawable.subscription_option_unchoose);
        root.setBackgroundResource(R.drawable.subscription_option_bg_unchoose);
    }
}
