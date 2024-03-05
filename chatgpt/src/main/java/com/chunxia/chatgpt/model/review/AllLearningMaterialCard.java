package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AllLearningMaterialCard implements Parcelable {

    private ArrayList<SentenceCard> sentenceCards = new ArrayList<>();
    private ArrayList<TopicTestCard> topicTestCards = new ArrayList<>();

    private String topic = "";

    public AllLearningMaterialCard(ArrayList<SentenceCard> sentenceCards, ArrayList<TopicTestCard> topicTestCards) {
        this.sentenceCards = sentenceCards;
        this.topicTestCards = topicTestCards;
    }

    public AllLearningMaterialCard(ArrayList<SentenceCard> sentenceCards, ArrayList<TopicTestCard> topicTestCards, String topic) {
        this.sentenceCards = sentenceCards;
        this.topicTestCards = topicTestCards;
        this.topic = topic;
    }

    public AllLearningMaterialCard() {
    }

    public void addSentenceCard(SentenceCard sentenceCard) {
        if(sentenceCards == null || sentenceCard == null) return;
        sentenceCards.add(sentenceCard);
    }

    public void addTopicTestCard(TopicTestCard topicTestCard){
        if (topicTestCards == null || topicTestCard == null) return;
        topicTestCards.add(topicTestCard);
    }

    public void addSentenceCard(ArrayList<SentenceCard> list) {
        if(sentenceCards == null || list == null) return;
        sentenceCards.addAll(list);
    }

    public void addTopicTestCard(ArrayList<TopicTestCard> list){
        if (topicTestCards == null || list == null) return;
        topicTestCards.addAll(list);
    }


    public int size() {
        int size1 = 0;
        int size2 = 0;
        if (sentenceCards == null) {
            size1 = 0;
        } else {
            size1 = sentenceCards.size();
        }
        if (topicTestCards == null) {
            size2 = 0;
        } else {
            size2 = topicTestCards.size();
        }
        return size1 + size2;
    }




    public ArrayList<SentenceCard> getSentenceCards() {
        return sentenceCards;
    }

    public void setSentenceCards(ArrayList<SentenceCard> sentenceCards) {
        this.sentenceCards = sentenceCards;
    }

    public ArrayList<TopicTestCard> getTopicTestCards() {
        return topicTestCards;
    }

    public void setTopicTestCards(ArrayList<TopicTestCard> topicTestCards) {
        this.topicTestCards = topicTestCards;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.sentenceCards);
        dest.writeTypedList(this.topicTestCards);
        dest.writeString(this.topic);
    }

    public void readFromParcel(Parcel source) {
        this.sentenceCards = source.createTypedArrayList(SentenceCard.CREATOR);
        this.topicTestCards = source.createTypedArrayList(TopicTestCard.CREATOR);
        this.topic = source.readString();
    }

    protected AllLearningMaterialCard(Parcel in) {
        this.sentenceCards = in.createTypedArrayList(SentenceCard.CREATOR);
        this.topicTestCards = in.createTypedArrayList(TopicTestCard.CREATOR);
        this.topic = in.readString();
    }

    public static final Creator<AllLearningMaterialCard> CREATOR = new Creator<AllLearningMaterialCard>() {
        @Override
        public AllLearningMaterialCard createFromParcel(Parcel source) {
            return new AllLearningMaterialCard(source);
        }

        @Override
        public AllLearningMaterialCard[] newArray(int size) {
            return new AllLearningMaterialCard[size];
        }
    };
}
