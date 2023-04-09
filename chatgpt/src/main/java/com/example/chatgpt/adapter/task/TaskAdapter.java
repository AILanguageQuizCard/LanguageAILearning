package com.example.chatgpt.adapter.task;

import static com.example.chatgpt.activity.ActivityIntentKeys.BEFORE_USER_MESSAGE_COMMAND;
import static com.example.chatgpt.activity.ActivityIntentKeys.CHAT_ACTIVITY_START_MODE;
import static com.example.chatgpt.activity.ActivityIntentKeys.START_WORDS;
import static com.example.chatgpt.activity.ActivityIntentKeys.SYSTEM_COMMAND;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.chatgpt.R;
import com.example.chatgpt.activity.ChatGptChatActivity;
import com.example.chatgpt.common.XLIntent;
import com.example.chatgpt.ui.TopicView2;

import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TaskAdapter";

    private final List<TopicInfo> items;
    private List<Integer> favoriteData;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskAdapter( List<TopicInfo> data) {
        items = new ArrayList<>(data);
        setFavoriteData(items);
    }

    private void setFavoriteData(List<TopicInfo> datas) {
        favoriteData = new ArrayList<>();
        int count = 0;
        for (TopicInfo topicInfo: datas ) {
            if(topicInfo.isFavorite()) {
                favoriteData.add(count, 0);
            } else {
                favoriteData.add(count);
            }
            count = count + 1;
        }
    }

//    private void like2unlike(TopicInfo topicInfo) {
//        items.remove(topicInfo);
//        int m = topicInfo.getCount();
//        int result = 0 ;
//        for (int i = items.size() - 1; i >= 0 ; i--) {
//            int n = items.get(i).getCount();
//            if (n < m) {
//                result = i + 1;
//                break;
//            }
//        }
//        items.add(result, topicInfo);
//        notifyDataSetChanged();
//    }
//
//    private void unlike2like(TopicInfo topicInfo) {
//        items.remove(topicInfo);
//        items.add(0 , topicInfo);
//        notifyDataSetChanged();
//    }


    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleView;
        public TextView descriptionView;
        public ImageView favoriteView;
        public TopicView2 topicView;

        public TaskItemViewHolder(View v) {
            super(v);
            topicView = (TopicView2) v;
            titleView = v.findViewById(R.id.title_textview);
            descriptionView = v.findViewById(R.id.description_textview);
            favoriteView =  v.findViewById(R.id.favorite_button);
        }
    }


    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = new TopicView2(parent.getContext());
        vh = new TaskItemViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TaskItemViewHolder) {
            TopicInfo topicInfo = items.get(position);
            TaskItemViewHolder taskItemViewHolder = (TaskItemViewHolder) holder;

            taskItemViewHolder.topicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new XLIntent(ActivityUtils.getTopActivity(), ChatGptChatActivity.class)
                            .putInt(CHAT_ACTIVITY_START_MODE, topicInfo.getStartMode())
                            .putString(START_WORDS, topicInfo.getStartWords())
                            .putString(SYSTEM_COMMAND, topicInfo.getSystemCommand())
                            .putString(BEFORE_USER_MESSAGE_COMMAND, topicInfo.getBeforeUserCommand());
                    ActivityUtils.getTopActivity().startActivity(intent);
                }
            });

            taskItemViewHolder.titleView.setText(topicInfo.getTitle());
            taskItemViewHolder.descriptionView.setText(topicInfo.getDescription());
            taskItemViewHolder.topicView.setBackgroundResource( topicInfo.getBgDrawableId());
            if (topicInfo.isFavorite()) {
                taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark_liked);
            } else {
                taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark);
            }
            taskItemViewHolder.favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(topicInfo.isFavorite()) {
                        topicInfo.setFavorite(false);
                        taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark);
//                        like2unlike(topicInfo);
                    } else {
                        topicInfo.setFavorite(true);
                        taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark_liked);
//                        unlike2like(topicInfo);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


}