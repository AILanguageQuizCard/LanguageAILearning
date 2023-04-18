package com.example.chatgpt.model;

import java.io.Serializable;

public class Message implements Serializable{
	private long id;
	private String date;
	private boolean fromMe;
	private boolean showTime = true;

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
}