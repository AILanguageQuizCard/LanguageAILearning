package com.chunxia.chatgpt.model.topic;

import android.content.Context;
import android.content.res.Resources;

import com.chunxia.chatgpt.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class TrainingTopicManager {
    private static ArrayList<String> data = null;

    public static final String TRAINING_TOPIC_KEY = "training_topic_key";

    public static ArrayList<String> getTrainingTopicList(Context context) {
        if (data!=null) return data;
        // 在你的Activity或Fragment中
        Resources res = context.getResources();
        String[] topicList = res.getStringArray(R.array.training_topic_list);
        data = new ArrayList<>(Arrays.asList(topicList));
        return data;
    }

    public static ArrayList<String> getRandomTopicList(int n, Context context) {
        ArrayList<String> topicList = getTrainingTopicList(context);
        ArrayList<String> result =  getRandomElements(topicList, n);

        // Sort the list by string length
        result.sort(new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });
        return result;
    }


    private static ArrayList<String> getRandomElements(ArrayList<String> list, int totalItems) {
        Collections.shuffle(list);
        ArrayList<String> newList = new ArrayList<String>();
        for (int i = 0; i < totalItems; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }


}
