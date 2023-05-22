package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ReviewRecord implements Parcelable {
    private Date time;
    private LearnRecord.ReviewGrade lastReviewedGrade;

    public ReviewRecord(Date time, LearnRecord.ReviewGrade lastReviewedGrade) {
        this.time = time;
        this.lastReviewedGrade = lastReviewedGrade;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public LearnRecord.ReviewGrade getLastReviewedGrade() {
        return lastReviewedGrade;
    }

    public void setLastReviewedGrade(LearnRecord.ReviewGrade lastReviewedGrade) {
        this.lastReviewedGrade = lastReviewedGrade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
        dest.writeInt(this.lastReviewedGrade == null ? -1 : this.lastReviewedGrade.ordinal());
    }

    public void readFromParcel(Parcel source) {
        long tmpTime = source.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        int tmpLastReviewedGrade = source.readInt();
        this.lastReviewedGrade = tmpLastReviewedGrade == -1 ? null : LearnRecord.ReviewGrade.values()[tmpLastReviewedGrade];
    }

    protected ReviewRecord(Parcel in) {
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        int tmpLastReviewedGrade = in.readInt();
        this.lastReviewedGrade = tmpLastReviewedGrade == -1 ? null : LearnRecord.ReviewGrade.values()[tmpLastReviewedGrade];
    }

    public static final Creator<ReviewRecord> CREATOR = new Creator<ReviewRecord>() {
        @Override
        public ReviewRecord createFromParcel(Parcel source) {
            return new ReviewRecord(source);
        }

        @Override
        public ReviewRecord[] newArray(int size) {
            return new ReviewRecord[size];
        }
    };
}
