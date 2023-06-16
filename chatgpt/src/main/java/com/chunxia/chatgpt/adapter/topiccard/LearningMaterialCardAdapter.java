package com.chunxia.chatgpt.adapter.topiccard;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.model.review.AllLearningMaterialCard;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;
import com.chunxia.chatgpt.ui.ExpandTextView;
import com.chunxia.chatgpt.voicerecord.models.Events;

import org.greenrobot.eventbus.EventBus;

public class LearningMaterialCardAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater = null;
    private final Text2VoiceModel text2VoiceModel;
    private final Context context;

    private final AllLearningMaterialCard learningMaterialCard;

    public LearningMaterialCardAdapter(Application application, AllLearningMaterialCard learningMaterialCard) {
        this.context = application;
        text2VoiceModel = new Text2VoiceModel(application);
        this.learningMaterialCard = learningMaterialCard;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View view = layoutInflater.inflate(R.layout.item_card_wizard, container, false);
        ((TextView) view.findViewById(R.id.topic_card_title)).setText(learningMaterialCard.getTopic());
        TextView sentenceView = view.findViewById(R.id.topic_card_sentence);
        sentenceView.setText(learningMaterialCard.getSentenceCards().get(position).getSentence());
        TextView translateView = view.findViewById(R.id.topic_card_translation);
        String translation = learningMaterialCard.getSentenceCards().get(position).getTranslation();
        ExpandTextView expandTextView = new ExpandTextView(context);
        expandTextView.setMaxLine(5)
                .setMargin(R.dimen.training_card_left_right_margin)
                .setColorStr("#346CE9");
        expandTextView.show(translateView, translation);
        ImageView playButton = view.findViewById(R.id.topic_card_play_button);
        ImageView favoriteButton = view.findViewById(R.id.topic_card_favorite_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2VoiceModel.onSpeak(learningMaterialCard.getSentenceCards().get(position).getSentence());
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewCardManager.getInstance().addOneSentenceCardInTopicReviewSets(learningMaterialCard.getTopic(),
                        learningMaterialCard.getSentenceCards().get(position));

                EventBus.getDefault().post(new Events.ShowSnackBar(context.getString(R.string.train_card_add_to_review_set_successfully)));

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return learningMaterialCard.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

