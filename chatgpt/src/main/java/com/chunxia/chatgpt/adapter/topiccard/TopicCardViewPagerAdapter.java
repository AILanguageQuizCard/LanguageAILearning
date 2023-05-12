package com.chunxia.chatgpt.adapter.topiccard;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.blankj.utilcode.util.ThreadUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;
import com.chunxia.deepl.DeepLTranslator;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class TopicCardViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;

    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> sentencesList = new ArrayList<>();

    private Text2VoiceModel text2VoiceModel;
    private Context context;

    public TopicCardViewPagerAdapter(Application application, ArrayList<String> titleList, ArrayList<String> sentencesList) {
        this.context = application;
        this.titleList = titleList;
        this.sentencesList = sentencesList;
        text2VoiceModel = new Text2VoiceModel(application);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_card_wizard, container, false);
        ((TextView) view.findViewById(R.id.topic_card_title)).setText(titleList.get(position));
        ((TextView) view.findViewById(R.id.topic_card_sentence)).setText(sentencesList.get(position));
        TextView translateView = view.findViewById(R.id.topic_card_translation);
        ThreadUtils.getFixedPool(5).submit(() -> {
            String result = DeepLTranslator.getInstance().translateText(sentencesList.get(position), "ZH");
            ThreadUtils.getMainHandler().post(() -> translateView.setText(result));
        });

        Button playButton = view.findViewById(R.id.topic_card_play_button);
        Button favoriteButton = view.findViewById(R.id.topic_card_favorite_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2VoiceModel.onSpeak(sentencesList.get(position));
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return titleList.size();
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