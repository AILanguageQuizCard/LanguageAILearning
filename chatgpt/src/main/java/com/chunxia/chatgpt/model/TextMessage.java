package com.chunxia.chatgpt.model;

public class TextMessage extends Message{
    private String content;


    public TextMessage(long id, String content, boolean fromMe, boolean showTime, String date) {
        super(id, fromMe, showTime, date);
        this.content = content;
        messageType = "TextMessage";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
