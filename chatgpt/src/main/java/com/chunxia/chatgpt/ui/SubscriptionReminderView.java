package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.chunxia.chatgpt.R;

public class SubscriptionReminderView extends LinearLayout {
    private ImageView iconView;

    private TextView titleView;


    public SubscriptionReminderView(Context context) {
        this(context,null);
    }

    public SubscriptionReminderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscriptionReminderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SubscriptionReminderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View view =  LayoutInflater.from(context).inflate(R.layout.subscription_reminder_view, this, true);
        iconView = view.findViewById(R.id.subscription_reminder_view_icon);
        titleView = view.findViewById(R.id.subscription_reminder_view_title);
    }

    public void setTitle(int n){
        String s = getResources().getString(R.string.subscription_reminder_view_text1);
        s = s + n;
        s = s + getResources().getString(R.string.subscription_reminder_view_text2);
        if (titleView != null) {
            titleView.setText(s);
        }
    }

}
