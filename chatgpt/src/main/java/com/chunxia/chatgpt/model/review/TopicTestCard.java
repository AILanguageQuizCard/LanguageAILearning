package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;

public class TopicTestCard implements Parcelable {


    private String question;
    private String answer;
    private String voicePath;
    private LearnRecord learnRecord;

    private String topic;

    public TopicTestCard() {
    }


    public TopicTestCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public LearnRecord getLearnRecord() {
        return learnRecord;
    }

    public void setLearnRecord(LearnRecord learnRecord) {
        this.learnRecord = learnRecord;
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
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeString(this.voicePath);
        dest.writeParcelable(this.learnRecord, flags);
        dest.writeString(this.topic);
    }

    public void readFromParcel(Parcel source) {
        this.question = source.readString();
        this.answer = source.readString();
        this.voicePath = source.readString();
        this.learnRecord = source.readParcelable(LearnRecord.class.getClassLoader());
        this.topic = source.readString();
    }


    protected TopicTestCard(Parcel in) {
        this.question = in.readString();
        this.answer = in.readString();
        this.voicePath = in.readString();
        this.learnRecord = in.readParcelable(LearnRecord.class.getClassLoader());
        this.topic = in.readString();
    }

    public static final Creator<TopicTestCard> CREATOR = new Creator<TopicTestCard>() {
        @Override
        public TopicTestCard createFromParcel(Parcel source) {
            return new TopicTestCard(source);
        }

        @Override
        public TopicTestCard[] newArray(int size) {
            return new TopicTestCard[size];
        }
    };
}
