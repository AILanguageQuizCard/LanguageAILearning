package com.example.chatgpt.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.chatgpt.R;

public class TopicView extends RelativeLayout {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView favoriteIcon;

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
        View view = LayoutInflater.from(context).inflate(R.layout.topic_view, this, true);

        // Find views by their IDs
        titleTextView = view.findViewById(R.id.title_textview);
        descriptionTextView = view.findViewById(R.id.description_textview);
        favoriteIcon = view.findViewById(R.id.favorite_button);

        // Handle custom XML attributes
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TopicView, 0, 0);
        String title = a.getString(R.styleable.TopicView_title);
        String description = a.getString(R.styleable.TopicView_description);
        a.recycle();

        setTitle(title);
        setDescription(description);

    }

    public void setTitle(String title) {
        if (title != null) {
            titleTextView.setText(title);
        } else {
            titleTextView.setText("Default Title");
        }
    }

    public void setDescription(String description) {
        if (description != null) {
            descriptionTextView.setText(description);
        } else {
            descriptionTextView.setText("Default Description");
        }
    }

    public void setFavoriteButtonOnClickListener(OnClickListener listener) {
        favoriteIcon.setOnClickListener(listener);
    }

}