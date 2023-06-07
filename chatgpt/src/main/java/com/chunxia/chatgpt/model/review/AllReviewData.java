package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AllReviewData implements Parcelable {

    public int size;

    public ArrayList<TopicReviewSets> topicReviewSetsList;

    public AllReviewData() {
        topicReviewSetsList = new ArrayList<>();
        size = 0;
    }

    public boolean topicExist(String topic) {
        return topicReviewSetsList.stream().anyMatch(a -> a.getTopic().equals(topic));
    }

    public void addOneSentenceCardInTopicReviewSets(String topic, SentenceCard sentenceCard) {
        if (topicExist(topic)) {
            IntStream.range(0, topicReviewSetsList.size())
                    .filter(i -> topicReviewSetsList.get(i).getTopic().equals(topic))
                    .forEach(i -> topicReviewSetsList.get(i).addOneSentenceCard(sentenceCard));
        } else {
            TopicReviewSets topicReviewSets = new TopicReviewSets(topic);
            topicReviewSets.addOneSentenceCard(sentenceCard);
            topicReviewSetsList.add(topicReviewSets);
            size++;
        }
    }

    public void addOneTopicReviewSet(TopicReviewSets topicReviewSets) {
        if (topicExist(topicReviewSets.getTopic())) {
            IntStream.range(0, topicReviewSetsList.size())
                    .filter(i -> topicReviewSetsList.get(i).getTopic().equals(topicReviewSets.getTopic()))
                    .forEach(i -> topicReviewSetsList.set(i, topicReviewSets));
        } else {
            topicReviewSetsList.add(topicReviewSets);
            size++;
        }
    }

    public List<String> getAllTopics() {
        return topicReviewSetsList.stream().map(TopicReviewSets::getTopic).collect(Collectors.toList());
    }


    public ArrayList<SentenceCard> getAllLearnCards() {
        ArrayList<SentenceCard> sentenceCards = new ArrayList<>();
        for (TopicReviewSets topicReviewSets : topicReviewSetsList) {
            sentenceCards.addAll(topicReviewSets.getSentenceCardList());
        }
        return sentenceCards;
    }

    public ArrayList<SentenceCard> getSentencesCardsByTopic(String topic) {
        ArrayList<SentenceCard> sentenceCards = new ArrayList<>();
        topicReviewSetsList.stream()
                .filter(a -> a.getTopic().equals(topic))
                .forEach(a -> sentenceCards.addAll(a.getSentenceCardList()));
        return sentenceCards;
    }





    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<TopicReviewSets> getTopicReviewSetsList() {
        return topicReviewSetsList;
    }

    public void setTopicReviewSetsList(ArrayList<TopicReviewSets> topicReviewSetsList) {
        this.topicReviewSetsList = topicReviewSetsList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.size);
        dest.writeTypedList(this.topicReviewSetsList);
    }

    public void readFromParcel(Parcel source) {
        this.size = source.readInt();
        this.topicReviewSetsList = source.createTypedArrayList(TopicReviewSets.CREATOR);
    }

    protected AllReviewData(Parcel in) {
        this.size = in.readInt();
        this.topicReviewSetsList = in.createTypedArrayList(TopicReviewSets.CREATOR);
    }

    public static final Creator<AllReviewData> CREATOR = new Creator<AllReviewData>() {
        @Override
        public AllReviewData createFromParcel(Parcel source) {
            return new AllReviewData(source);
        }

        @Override
        public AllReviewData[] newArray(int size) {
            return new AllReviewData[size];
        }
    };
}
