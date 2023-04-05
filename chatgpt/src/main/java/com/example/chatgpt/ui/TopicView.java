package com.example.chatgpt.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.chatgpt.R;

public class TopicView extends RelativeLayout {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button favoriteButton;

    public TopicView(Context context) {
        this(context, null);
    }

    public TopicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {


    }


    private void initView(Context context, AttributeSet attrs) {
        // 为什么集成View 而不是ConstraintLayout 这里会报错？
//        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setLayoutParams(lp1);

        View view = LayoutInflater.from(context).inflate(R.layout.topic_view, this, true);

        // Find views by their IDs
        titleTextView = view.findViewById(R.id.title_textview);
        descriptionTextView = view.findViewById(R.id.description_textview);
        favoriteButton = view.findViewById(R.id.favorite_button);

        // Set default values for views
//        titleTextView.setText("Default Title");
        descriptionTextView.setText("Default Description");

        // Handle custom XML attributes
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TopicView, 0, 0);
        String title = a.getString(R.styleable.TopicView_title);
        String description = a.getString(R.styleable.TopicView_description);
//        int backgroundColor = a.getColor(R.styleable.TopicView_backgroundColor, Color.WHITE);
        a.recycle();

        if (title != null) {
            titleTextView.setText(title);
        }
        if (description != null) {
            descriptionTextView.setText(description);
        }
//        setBackgroundColor(backgroundColor);

    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setDescription(String description) {
        descriptionTextView.setText(description);
    }

    public void setFavoriteButtonOnClickListener(OnClickListener listener) {
        favoriteButton.setOnClickListener(listener);
    }

//    public void setBackgroundColor(int color) {
//        findViewById(R.id.custom_view).setBackgroundColor(color);
//    }
}