package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunxia.chatgpt.R;

public class LanguageItemView extends RelativeLayout {

    private TextView titleView;
    private ImageView iconView;

    public LanguageItemView(Context context) {
        this(context, null);
    }

    public LanguageItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LanguageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {


    }


    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_language_view, this, true);

        // Find views by their IDs
        titleView = view.findViewById(R.id.language_textview);
        iconView = view.findViewById(R.id.language_choose_view);

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
