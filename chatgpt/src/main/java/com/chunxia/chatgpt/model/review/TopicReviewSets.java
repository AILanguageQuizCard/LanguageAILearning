package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class TopicReviewSets implements Parcelable {

    @NonNull
    public String topic;

    // todo SentenceCard应该改成接口，有两个实现类，一个是SentenceCard，一个是TopicTestCard
    public ArrayList<SentenceCard> sentenceCardList;


    public TopicReviewSets(String topic, ArrayList<SentenceCard> sentenceCardList) {
        this.topic = topic;
        this.sentenceCardList = sentenceCardList;
    }


    public TopicReviewSets(String topic) {
        this.topic = topic;
        sentenceCardList = new ArrayList<>();
    }

    public void update() {
        TopicReviewSets newVersion = ReviewCardManager.getInstance().getTopicReviewSetsByTopic(topic);
        if (newVersion == null) {
            // 上层应该处理对应话题已经被删除的情况
            sentenceCardList = new ArrayList<>();
        } else {
            this.sentenceCardList = newVersion.sentenceCardList;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<SentenceCard> getSentenceCardList() {
        return sentenceCardList;
    }

    public void setSentenceCardList(ArrayList<SentenceCard> sentenceCardList) {
        this.sentenceCardList = sentenceCardList;
    }

    public void addOneSentenceCard(SentenceCard sentenceCard) {
        if(sentenceCardList == null || sentenceCard == null) return;
        sentenceCardList.add(sentenceCard);
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topic);
        dest.writeTypedList(this.sentenceCardList);
    }

    public void readFromParcel(Parcel source) {
        this.topic = source.readString();
        this.sentenceCardList = source.createTypedArrayList(SentenceCard.CREATOR);
    }

    public TopicReviewSets() {
    }

    protected TopicReviewSets(Parcel in) {
        this.topic = in.readString();
        this.sentenceCardList = in.createTypedArrayList(SentenceCard.CREATOR);
    }

    public static final Creator<TopicReviewSets> CREATOR = new Creator<TopicReviewSets>() {
        @Override
        public TopicReviewSets createFromParcel(Parcel source) {
            return new TopicReviewSets(source);
        }

        @Override
        public TopicReviewSets[] newArray(int size) {
            return new TopicReviewSets[size];
        }
    };
}
