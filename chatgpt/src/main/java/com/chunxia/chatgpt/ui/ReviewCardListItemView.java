package com.chunxia.chatgpt.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chunxia.chatgpt.R;

public class ReviewCardListItemView extends ConstraintLayout {

    private TextView topicView;

    private TextView unReviewCountView;

    private TextView reviewingCountView;

    private TextView reviewedCountView;


    public ReviewCardListItemView(Context context) {
        this(context, null);
    }

    public ReviewCardListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewCardListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

    }

    private ImageView menuIcon;

    private TextView lastReviewTimeView;

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_card_list_view, this, true);

        topicView = view.findViewById(R.id.review_card_list_view_topic);
        menuIcon = view.findViewById(R.id.review_card_list_view_menu_icon);

        unReviewCountView = view.findViewById(R.id.review_card_list_view_not_learn_number);
        reviewingCountView = view.findViewById(R.id.review_card_list_view_learning_number);
        reviewedCountView = view.findViewById(R.id.review_card_list_view_already_learn_number);

        lastReviewTimeView = view.findViewById(R.id.review_card_list_view_last_learn_time_number);
    }

    public void setTopic(String title) {
        if (title != null) {
            topicView.setText(title);
        } else {
            topicView.setText("Default Title");
        }
    }


    public void setUnReviewCount(int count) {
        unReviewCountView.setText(String.valueOf(count));
    }

    public void setReviewingCount(int count) {
        reviewingCountView.setText(String.valueOf(count));
    }

    public void setReviewedCount(int count) {
        reviewedCountView.setText(String.valueOf(count));
    }

}
