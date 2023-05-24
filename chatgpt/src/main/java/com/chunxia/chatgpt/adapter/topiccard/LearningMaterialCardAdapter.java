package com.chunxia.chatgpt.adapter.topiccard;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.model.review.AllLearningMaterialCard;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;

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
        ((TextView) view.findViewById(R.id.topic_card_sentence)).setText(learningMaterialCard.getSentenceCards().get(position).getSentence());
        TextView translateView = view.findViewById(R.id.topic_card_translation);
        translateView.setText(learningMaterialCard.getSentenceCards().get(position).getTranslation());
        Button playButton = view.findViewById(R.id.topic_card_play_button);
        Button favoriteButton = view.findViewById(R.id.topic_card_favorite_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2VoiceModel.onSpeak(learningMaterialCard.getSentenceCards().get(position).getSentence());
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