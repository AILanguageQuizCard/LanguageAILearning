package com.chunxia.chatgpt.adapter.settingItem;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.chunxia.chatgpt.mmkv.MMKVConstant;
import com.chunxia.chatgpt.ui.LanguageItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class VoiceLanguageSettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TaskAdapter";

    private List<SettingInfo> items;
    private List<Integer> favoriteData;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VoiceLanguageSettingAdapter(ArrayList<SettingInfo> data) {
        items = data;
    }


    public static class SettingItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView languageView;
        public ImageView chooseView;
        public LanguageItemView root;

        public SettingItemViewHolder(View v) {
            super(v);
            root = (LanguageItemView) v;
            languageView = v.findViewById(R.id.language_textview);
            chooseView = v.findViewById(R.id.language_choose_view);
        }

        public void setOnViewClick(View.OnClickListener onViewClick) {
            root.setOnClickListener(onViewClick);
        }

        public void setChooseViewVisible(Boolean b) {
            if (b) chooseView.setVisibility(View.VISIBLE);
            else chooseView.setVisibility(View.GONE);
        }

        public void setLanguage(String s) {
            languageView.setText(s);
        }
    }


    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = new LanguageItemView(parent.getContext());
        vh = new SettingItemViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SettingItemViewHolder) {
            SettingInfo settingInfo = items.get(position);
            SettingItemViewHolder settingItemViewHolder = (SettingItemViewHolder) holder;
            settingItemViewHolder.setLanguage(settingInfo.getTitle());
            if (settingInfo.isChoosed()) {
                settingItemViewHolder.setChooseViewVisible(true);
                CXMMKV.getMMKV().putString(MMKVConstant.SETTING_VOICE_LANGUAGE_KEY, settingInfo.getTitle());
            } else {
                settingItemViewHolder.setChooseViewVisible(false);
            }
            settingItemViewHolder.setOnViewClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDataLanguageChoosed(settingInfo.getTitle());
                }
            });
        }
    }

    private void setDataLanguageChoosed(String language) {
        items = items.stream().peek(settingInfo1 -> {
            if (settingInfo1.getTitle().equals(language)) {
                settingInfo1.setChoosed(true);
            } else {
                settingInfo1.setChoosed(false);
            }
        }).collect(Collectors.toList());
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}