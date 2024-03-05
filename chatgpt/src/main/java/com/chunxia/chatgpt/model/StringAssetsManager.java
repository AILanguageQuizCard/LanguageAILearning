package com.chunxia.chatgpt.model;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public abstract class StringAssetsManager {
    protected ArrayList<String> data = null;
    protected int arrayId;

    public StringAssetsManager(int arrayId) {
        this.arrayId = arrayId;
    }

    public ArrayList<String> getTopicList(Context context) {
        if (data != null) return data;
        Resources res = context.getResources();
        String[] topicList = res.getStringArray(arrayId);
        data = new ArrayList<>(Arrays.asList(topicList));
        return data;
    }

    public ArrayList<String> getRandomTopicList(int n, Context context) {
        ArrayList<String> topicList = getTopicList(context);
        ArrayList<String> result = getRandomElements(topicList, n);

        // Sort the list by string length
        result.sort(Comparator.comparingInt(String::length));
        return result;
    }

    protected ArrayList<String> getRandomElements(ArrayList<String> list, int totalItems) {
        Collections.shuffle(list);
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }
}
