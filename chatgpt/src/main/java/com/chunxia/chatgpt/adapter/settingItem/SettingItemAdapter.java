package com.chunxia.chatgpt.adapter.settingItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.ui.TopicView2;

import java.util.ArrayList;
import java.util.List;


public class SettingItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TaskAdapter";

    private final List<SettingInfo> items;
    private List<Integer> favoriteData;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SettingItemAdapter(List<SettingInfo> data) {
        items = new ArrayList<>(data);

    }


    public static class SettingItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView languageView;
        public ImageView chooseView;

        public SettingItemViewHolder(View v) {
            super(v);
            languageView = v.findViewById(R.id.title_textview);
            chooseView = v.findViewById(R.id.favorite_button);
        }
    }


    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = new TopicView2(parent.getContext());
        vh = new SettingItemViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SettingItemViewHolder) {

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}