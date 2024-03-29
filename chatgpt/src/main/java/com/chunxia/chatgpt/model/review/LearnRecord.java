package com.chunxia.chatgpt.model.review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


// todo 三个剩余重要的部分 收入系统 教学 安全
public class LearnRecord implements Parcelable {
    private static final int[] REVIEW_SCHEDULE = {1, 5, 15};  // Review schedule in days

    private List<ReviewRecord> reviewRecordList = new ArrayList<>();

    public LearnRecord() {
    }


    public List<ReviewRecord> getReviewRecordList() {
        return reviewRecordList;
    }

    public Date getLatestReviewTime() {
        // 返回reviewRecordList 列表中，离当前时间最近的时间
        return reviewRecordList.stream()
                .map(ReviewRecord::getTime)
                .max(Comparator.naturalOrder())
                .orElse(null);  // 返回 null 如果列表为空
    }


    public void setReviewRecordList(List<ReviewRecord> reviewRecordList) {
        this.reviewRecordList = reviewRecordList;
    }

    public void addGoodReview() {
        ReviewRecord reviewRecord = new ReviewRecord(new Date(), ReviewGrade.GOOD);
        this.reviewRecordList.add(reviewRecord);
    }

    public void addBadReview() {
        ReviewRecord reviewRecord = new ReviewRecord(new Date(), ReviewGrade.BAD);
        this.reviewRecordList.add(reviewRecord);
    }


    public enum ReviewGrade {
        GOOD,
        BAD
    }

    // todo 复习系统到底应该怎么设计？
//    public boolean reviewed() {
//        return !reviewRecordList.isEmpty();
//    }

    public boolean shouldReview() {
        if (reviewRecordList.isEmpty()) {
            return true;
        }

        // Getting the last review record
        ReviewRecord lastRecord = reviewRecordList.get(reviewRecordList.size() - 1);

        // If last review was BAD, then should review and reset all previous records
        if (lastRecord.getLastReviewedGrade() == ReviewGrade.BAD) {
//            resetReviewRecords();
            return true;
        }

        // If last review was GOOD, check the time to decide
        Date lastReviewDate = lastRecord.getTime();

        // Calculate the difference between current time and last review time in days
        long diffInMillies = Math.abs(new Date().getTime() - lastReviewDate.getTime());
        long daysSinceLastReview = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        // If it's been more than required days since last review, then should review
        if (daysSinceLastReview >= getDaysForNextReview()) {
            return true;
        }

        return false;
    }

    // Returns number of days for next review based on the review schedule
    private int getDaysForNextReview() {
        if (reviewRecordList.size() < REVIEW_SCHEDULE.length) {
            return REVIEW_SCHEDULE[reviewRecordList.size()];
        } else {
            return Integer.MAX_VALUE;  // No more reviews needed
        }
    }

    // Reset all review records
    private void resetReviewRecords() {
        reviewRecordList.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.reviewRecordList);
    }

    public void readFromParcel(Parcel source) {
        this.reviewRecordList = new ArrayList<ReviewRecord>();
        source.readList(this.reviewRecordList, ReviewRecord.class.getClassLoader());
    }


    protected LearnRecord(Parcel in) {
        this.reviewRecordList = new ArrayList<ReviewRecord>();
        in.readList(this.reviewRecordList, ReviewRecord.class.getClassLoader());
    }

    public static final Creator<LearnRecord> CREATOR = new Creator<LearnRecord>() {
        @Override
        public LearnRecord createFromParcel(Parcel source) {
            return new LearnRecord(source);
        }

        @Override
        public LearnRecord[] newArray(int size) {
            return new LearnRecord[size];
        }
    };
}

