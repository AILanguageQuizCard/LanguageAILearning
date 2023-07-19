package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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


    public int size() {
        if (sentenceCardList == null) return 0;
        return sentenceCardList.size();
    }

    public Date getLatestReviewTime() {
        // 返回reviewRecordList 列表中，离当前时间最近的时间
        return sentenceCardList.stream()
                .map(SentenceCard::getLatestReviewTime)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);  // 返回 null 如果列表为空
    }

    public static class ReviewData {
        public int reviewedNumber;
        public int unReviewedNumber;
        public int reviewingNumber;

        public ReviewData(int unReviewedNumber,int reviewingNumber, int reviewedNumber) {
            this.reviewedNumber = reviewedNumber;
            this.unReviewedNumber = unReviewedNumber;
            this.reviewingNumber = reviewingNumber;
        }
    }

    public ReviewData getReviewNumber() {
        int reviewedNumber = 0;
        int unReviewedNumber = 0;
        int reviewingNumber = 0;

        for (SentenceCard sentenceCard : sentenceCardList) {
            int reviewLevel = sentenceCard.getReviewLevel();
            switch (reviewLevel) {
                case 0:
                    unReviewedNumber++;
                    break;
                case 1:
                    reviewingNumber++;
                    break;
                case 2:
                    reviewedNumber++;
                    break;
            }
        }
        return new ReviewData(unReviewedNumber, reviewingNumber, reviewedNumber);
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

    public void addSentenceCardList(ArrayList<SentenceCard> sentenceCardList) {
        this.sentenceCardList.addAll(sentenceCardList);
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
        if (sentenceCardList == null || sentenceCard == null) return;
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
