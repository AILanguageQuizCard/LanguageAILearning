package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunxia.chatgpt.R;

public class SubscriptionDescriptionView extends RelativeLayout {

    private ImageView iconView;

    private TextView titleView;

    private TextView detailView;


    public SubscriptionDescriptionView(Context context) {
        this(context, null);
    }

    public SubscriptionDescriptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscriptionDescriptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.subscription_desctiption_item_view, this, true);
        iconView = view.findViewById(R.id.subscription_description_icon);
        titleView = view.findViewById(R.id.subscription_option_title_view);
        detailView = view.findViewById(R.id.subscription_description_detail);
    }

    public void setAll(int resId, String title, String detail){
        setIcon(resId);
        setTitle(title);
        setDetail(detail);
    }

    public void setIcon(int resId){
        iconView.setImageResource(resId);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setDetail(String detail){
        detailView.setText(detail);
    }


}
