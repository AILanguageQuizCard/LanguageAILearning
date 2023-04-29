package com.chunxia.chatgpt.adapter.training;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_RESULT_KEY;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.NORMAL_CHAT_MODE;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT1;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT2;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT3;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT4;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.ActivityIntentKeys;
import com.chunxia.chatgpt.activity.ChatGptChatActivity;
import com.chunxia.chatgpt.activity.TrainingCardActivity;
import com.chunxia.chatgpt.chatapi.MultiRoundChatAiApi;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.ui.TopicView2;
import com.material.components.activity.card.CardWizardOverlap;

import java.lang.ref.PhantomReference;
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


    private static ArrayList<String> extractSentences(String input) {
        ArrayList<String> sentences = new ArrayList<String>();
        String[] words = input.split("\\|\\|\\|"); // 按照 "|||" 进行分割
        for (String word : words) {
            int startIndex = word.lastIndexOf("***");
            int startIndex2 = startIndex + 3; // 获取内容的起始位置
            if (startIndex != - 1) {
                String sentence = word.substring(startIndex2); // 提取内容
                sentences.add(sentence);
            }
        }
        return sentences;
    }


    private void initTopicChat(String topic, String language, int num) {
        MultiRoundChatAiApi chatAgent = new MultiRoundChatAiApi("", NORMAL_CHAT_MODE);
        String prompt = TOPIC_TRAINING_PROMPT1 + topic
                + TOPIC_TRAINING_PROMPT2 + language
                + TOPIC_TRAINING_PROMPT3 + num
                + TOPIC_TRAINING_PROMPT4;
        chatAgent.sendMessageInThread(prompt, new MultiRoundChatAiApi.ReceiveOpenAiReply() {
            @Override
            public void onSuccess(String reply) {
                ArrayList<String> results = extractSentences(reply);

                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), TrainingCardActivity.class)
                        .putStringArrayList(TOPIC_TRAINING_RESULT_KEY, results);
                ActivityUtils.getTopActivity().startActivity(intent);
            }
        });
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
                        initTopicChat("Travelling", "English", 10);
                    } else if (trainingInfo.getType().equals(TrainingType.OPINION)) {


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