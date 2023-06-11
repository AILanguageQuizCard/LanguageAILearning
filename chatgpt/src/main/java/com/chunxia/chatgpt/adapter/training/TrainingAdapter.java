package com.chunxia.chatgpt.adapter.training;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.OpinionTrainingActivity;
import com.chunxia.chatgpt.activity.SentencePatternTrainingActivity;
import com.chunxia.chatgpt.activity.TopicTrainingActivity;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.ui.TopicView2;

import java.util.ArrayList;
import java.util.List;


public class TrainingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "TaskAdapter";

    private final List<TrainingInfo> items;
    private List<Integer> favoriteData;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TrainingAdapter(List<TrainingInfo> data) {
        items = new ArrayList<>(data);
        setFavoriteData(items);
    }

    private void setFavoriteData(List<TrainingInfo> datas) {
        favoriteData = new ArrayList<>();
        int count = 0;
        for (TrainingInfo trainingInfo : datas) {
            if (trainingInfo.isFavorite()) {
                favoriteData.add(count, 0);
            } else {
                favoriteData.add(count);
            }
            count = count + 1;
        }
    }


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
            favoriteView = v.findViewById(R.id.favorite_button);
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
            TrainingInfo trainingInfo = items.get(position);
            TaskItemViewHolder taskItemViewHolder = (TaskItemViewHolder) holder;

            taskItemViewHolder.topicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trainingInfo.getType().equals(TrainingType.TOPIC)) {
                        // todo 先跳转卡片,后续需要先加上话题输入界面
                        Intent intent = new XLIntent(ActivityUtils.getTopActivity(), TopicTrainingActivity.class);
                        ActivityUtils.getTopActivity().startActivity(intent);
                    } else if (trainingInfo.getType().equals(TrainingType.OPINION)) {
                        Intent intent = new XLIntent(ActivityUtils.getTopActivity(), OpinionTrainingActivity.class);
                        ActivityUtils.getTopActivity().startActivity(intent);
                    } else if (trainingInfo.getType().equals(TrainingType.SENTENCE_PATTERN)) {
                        Intent intent = new XLIntent(ActivityUtils.getTopActivity(), TopicTrainingActivity.class);
                        ActivityUtils.getTopActivity().startActivity(intent);
                    }
                }
            });

            taskItemViewHolder.titleView.setText(trainingInfo.getTitle());
            taskItemViewHolder.descriptionView.setText(trainingInfo.getDescription());
            taskItemViewHolder.topicView.setBackgroundResource(trainingInfo.getBgDrawableId());
            if (trainingInfo.isFavorite()) {
                taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark_liked);
            } else {
                taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark);
            }
            taskItemViewHolder.favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trainingInfo.isFavorite()) {
                        trainingInfo.setFavorite(false);
                        taskItemViewHolder.favoriteView.setImageResource(R.drawable.ic_bookmark);
//                        like2unlike(topicInfo);
                    } else {
                        trainingInfo.setFavorite(true);
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