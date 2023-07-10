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
        titleView = root.findViewById(R.id.subscription_option_title_view);
        priceView = root.findViewById(R.id.subscription_option_price_view);
        savedView = root.findViewById(R.id.subscription_option_save_view);
        savedView.setVisibility(GONE);
    }

    /**
     * 设置价格
     * @param price example: "$9.99/month" 入参三要素：货币符号、价格、周期
     */
    public void setPrice(String price) {
        String originalDescription = (String) priceView.getText();
        priceView.setText(String.format("%s %s", price, originalDescription));
    }

    public void setTitle(String title) {
        titleView.setText(title);
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
