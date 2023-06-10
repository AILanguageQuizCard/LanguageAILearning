package com.chunxia.chatgpt.adapter.chat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class ChoosedItem implements Parcelable {
    public int position;
    public String text;
    public ChoosedItem(int position, String text) {
        this.position = position;
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoosedItem that = (ChoosedItem) o;
        return position == that.position && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeString(this.text);
    }

    public void readFromParcel(Parcel source) {
        this.position = source.readInt();
        this.text = source.readString();
    }

    protected ChoosedItem(Parcel in) {
        this.position = in.readInt();
        this.text = in.readString();
    }

    public static final Creator<ChoosedItem> CREATOR = new Creator<ChoosedItem>() {
        @Override
        public ChoosedItem createFromParcel(Parcel source) {
            return new ChoosedItem(source);
        }

        @Override
        public ChoosedItem[] newArray(int size) {
            return new ChoosedItem[size];
        }
    };
}