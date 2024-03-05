package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunxia.chatgpt.R;

public class SettingItemView extends RelativeLayout {

    private TextView titleView;
    private ImageView iconView;
    private ImageView goView;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {


    }


    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_view, this, true);

        // Find views by their IDs
        titleView = view.findViewById(R.id.title_textview);
        iconView = view.findViewById(R.id.setting_view_icon);
        goView = view.findViewById(R.id.setting_view_go);

        // Handle custom XML attributes
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SettingItemView, 0, 0);
        String title = a.getString(R.styleable.SettingItemView_text);
        Drawable drawable = a.getDrawable(R.styleable.SettingItemView_icon);
        a.recycle();

        setTitle(title);
        setIconView(drawable);

    }

    public void setTitle(String title) {
        if (title != null) {
            titleView.setText(title);
        } else {
            titleView.setText("Default Title");
        }
    }

    private void setIconView(Drawable drawable) {
        iconView.setImageDrawable(drawable);
    }


}
