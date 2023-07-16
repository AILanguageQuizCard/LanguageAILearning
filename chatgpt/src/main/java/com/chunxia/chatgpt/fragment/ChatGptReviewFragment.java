package com.chunxia.chatgpt.fragment;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_MODE;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_MODE_ALL;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_MODE_SINGLE;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_TOPIC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.AddReviewCardActivity;
import com.chunxia.chatgpt.activity.ReviewCardActivity;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.model.review.AllReviewData;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.model.review.TopicReviewSets;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.ui.ReviewCardListItemView;
import com.google.android.material.button.MaterialButton;


public class ChatGptReviewFragment extends Fragment {

    private MaterialButton startReviewButton;
    private MaterialButton addYourOwnCardButton;
    private View root;

    public ChatGptReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review, container, false);
        this.root = root;
        initView();
        return root;
    }


    private void initStartReviewButton() {
        startReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicReviewSets topicReviewSets = ReviewCardManager.getInstance().getAllTopicReviewSets();
                ReviewCardManager.getInstance().setCurrentTopicReviewSets(topicReviewSets);
                Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class)
                        .putString(ACTIVITY_REVIEW_CARD_MODE, ACTIVITY_REVIEW_CARD_MODE_ALL);
                ActivityUtils.startActivity(intent);
            }
        });
    }

    private void initAddYourOwnCardButton() {
        addYourOwnCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), AddReviewCardActivity.class);
                ActivityUtils.startActivity(intent);
            }
        });
    }


    private void initView() {
        startReviewButton = root.findViewById(R.id.exerciseButton);
        initStartReviewButton();
        addYourOwnCardButton = root.findViewById(R.id.autoplayButton);
        initAddYourOwnCardButton();
        initReviewListViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        // todo 优化 全部刷新太耗时，只应该刷新修改的部分
        initReviewListViews();
    }

    private void initReviewListViews() {
        AllReviewData allReviewData = ReviewCardManager.getInstance().getAllReviewData();
        if(allReviewData == null) return;
        int size = allReviewData.getSize();
        if (size == 0) return;

        Activity activity = getActivity();

        LinearLayout container = root.findViewById(R.id.fragment_review_cards_container);
        container.removeAllViews();

        for (int i = 0; i < size; i++) {
            TopicReviewSets tempTopicReviewSets = allReviewData.getTopicReviewSetsList().get(i);
            TopicReviewSets.ReviewData reviewData = tempTopicReviewSets.getReviewNumber();
            String topic = tempTopicReviewSets.getTopic();

            ReviewCardListItemView reviewCardListItemView = new ReviewCardListItemView(activity);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                    LinearLayout.LayoutParams.WRAP_CONTENT // 高度
            );

            int margin = Tools.dip2px(activity, 10); // 将 dp 转换为像素
            layoutParams.setMargins(margin, margin, margin, 0);
//            reviewCardListItemView.setBackground(getResources().getDrawable(R.drawable.pay_view_blue, null));
            reviewCardListItemView.setTopic(topic);
            reviewCardListItemView.setUnReviewCount(reviewData.unReviewedNumber);
            reviewCardListItemView.setReviewingCount(reviewData.reviewingNumber);
            reviewCardListItemView.setReviewedCount(reviewData.reviewedNumber);
            reviewCardListItemView.setLatestReviewTime(tempTopicReviewSets.getLatestReviewTime());
            container.addView(reviewCardListItemView, layoutParams);

            reviewCardListItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TopicReviewSets topicReviewSets = ReviewCardManager.getInstance()
                            .getTopicReviewSetsByTopic(topic);

                    ReviewCardManager.getInstance().setCurrentTopicReviewSets(topicReviewSets);
                    Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class)
                            .putString(ACTIVITY_REVIEW_CARD_MODE, ACTIVITY_REVIEW_CARD_MODE_SINGLE)
                            .putString(ACTIVITY_REVIEW_CARD_TOPIC, topic);

                    ActivityUtils.startActivity(intent);
                }
            });
        }
    }
}
