package com.chunxia.chatgpt.adapter.topiccard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.chunxia.chatgpt.R;

import java.util.ArrayList;

public class TopicCardViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;

    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> sentencesList = new ArrayList<>();

    private Context context;

    public TopicCardViewPagerAdapter(Context context, ArrayList<String> titleList, ArrayList<String> sentencesList) {
        this.context = context;
        this.titleList = titleList;
        this.sentencesList = sentencesList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_card_wizard, container, false);
        ((TextView) view.findViewById(R.id.title)).setText(titleList.get(position));
        ((TextView) view.findViewById(R.id.description)).setText(sentencesList.get(position));

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