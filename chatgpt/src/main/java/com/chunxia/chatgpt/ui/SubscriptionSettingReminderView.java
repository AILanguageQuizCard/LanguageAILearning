package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chunxia.chatgpt.R;

public class SubscriptionSettingReminderView extends LinearLayout {
    private ImageView iconView;

    private TextView titleView;


    public SubscriptionSettingReminderView(Context context) {
        this(context,null);
    }

    public SubscriptionSettingReminderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscriptionSettingReminderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SubscriptionSettingReminderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view =  LayoutInflater.from(context).inflate(R.layout.subscription_setting_reminder_view, this, true);
        iconView = view.findViewById(R.id.subscription_reminder_view_icon);
        titleView = view.findViewById(R.id.subscription_reminder_view_title);
    }


    public void setTitle (String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

}
