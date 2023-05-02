package com.chunxia.chatgpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Parcelable {
	private long id;
	private String date;
	private boolean fromMe;
	private boolean showTime = true;

	protected String messageType;

	public Message(long id, boolean fromMe, String date) {
		this.id = id;
		this.date = date;
		this.fromMe = fromMe;
	}

	public Message(long id,  boolean fromMe, boolean showTime, String date) {
		this.id = id;
		this.date = date;
		this.fromMe = fromMe;
		this.showTime = showTime;
	}

	public long getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public boolean isFromMe() {
		return fromMe;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public boolean isVoice() {
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.date);
		dest.writeByte(this.fromMe ? (byte) 1 : (byte) 0);
		dest.writeByte(this.showTime ? (byte) 1 : (byte) 0);
	}

	public void readFromParcel(Parcel source) {
		this.id = source.readLong();
		this.date = source.readString();
		this.fromMe = source.readByte() != 0;
		this.showTime = source.readByte() != 0;
	}

	protected Message(Parcel in) {
		this.id = in.readLong();
		this.date = in.readString();
		this.fromMe = in.readByte() != 0;
		this.showTime = in.readByte() != 0;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setFromMe(boolean fromMe) {
		this.fromMe = fromMe;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
		@Override
		public Message createFromParcel(Parcel source) {
			return new Message(source);
		}

		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};
}