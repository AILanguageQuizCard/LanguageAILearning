package com.chunxia.chatgpt.subscription;

import android.os.Parcel;
import android.os.Parcelable;

public class SubscriptionData implements Parcelable {
    private int remainingTrials;

    public SubscriptionData(int remainingTrials) {
        this.remainingTrials = remainingTrials;
    }

    public int getRemainingTrials() {
        return remainingTrials;
    }

    public void setRemainingTrials(int remainingTrials) {
        this.remainingTrials = remainingTrials;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.remainingTrials);
    }

    public void readFromParcel(Parcel source) {
        this.remainingTrials = source.readInt();
    }

    protected SubscriptionData(Parcel in) {
        this.remainingTrials = in.readInt();
    }

    public static final Creator<SubscriptionData> CREATOR = new Creator<SubscriptionData>() {
        @Override
        public SubscriptionData createFromParcel(Parcel source) {
            return new SubscriptionData(source);
        }

        @Override
        public SubscriptionData[] newArray(int size) {
            return new SubscriptionData[size];
        }
    };
}
