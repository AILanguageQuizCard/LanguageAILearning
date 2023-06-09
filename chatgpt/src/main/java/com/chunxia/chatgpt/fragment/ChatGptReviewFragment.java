package com.chunxia.chatgpt.fragment;

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
import com.chunxia.chatgpt.model.review.SentenceCard;
import com.chunxia.chatgpt.model.review.TopicReviewSets;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.ui.ReviewCardListView;
import com.chunxia.chatgpt.ui.SettingItemView;

import java.util.ArrayList;


public class ChatGptReviewFragment extends Fragment {

    private SettingItemView startReviewButton;
    private SettingItemView addYourOwnCardButton;
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
//                ArrayList<SentenceCard> sentencesCardsByTopic = ReviewCardManager.getInstance()
//                        .getAllLearnCards();
//
//                ReviewCardManager.getInstance().setCurrentPresentingCards(sentencesCardsByTopic);
//                Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class);
//                ActivityUtils.startActivity(intent);
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
        startReviewButton = root.findViewById(R.id.start_review_view);
        initStartReviewButton();
        addYourOwnCardButton = root.findViewById(R.id.add_your_own_review_card_view);
        initAddYourOwnCardButton();

        initReviewListViews();
    }

    private void initReviewListViews() {
        AllReviewData allReviewData = ReviewCardManager.getInstance().getAllReviewData();
        if(allReviewData == null) return;
        int size = allReviewData.getSize();
        if (size == 0) return;

        Activity activity = getActivity();

        LinearLayout container = root.findViewById(R.id.fragment_review_cards_container);
        for (int i = 0; i < size; i++) {
            String topic = allReviewData.getTopicReviewSetsList().get(i).getTopic();

            ReviewCardListView reviewCardListView = new ReviewCardListView(activity);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                    (int) Tools.dip2px(activity, 50) // 高度
            );

            int margin = Tools.dip2px(activity, 10); // 将 dp 转换为像素
            layoutParams.setMargins(margin, margin, margin, 0);
            reviewCardListView.setBackground(getResources().getDrawable(R.drawable.pay_view_blue, null));
            reviewCardListView.setTopic(topic);
            container.addView(reviewCardListView, layoutParams);

            reviewCardListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TopicReviewSets topicReviewSets = ReviewCardManager.getInstance()
                            .getTopicReviewSetsByTopic(topic);

                    ReviewCardManager.getInstance().setCurrentTopicReviewSets(topicReviewSets);
                    Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class)
                            .putString(ACTIVITY_REVIEW_CARD_TOPIC, topic);
                    intent.putExtra("topic", topic);
                    ActivityUtils.startActivity(intent);
                }
            });
        }
    }


}
