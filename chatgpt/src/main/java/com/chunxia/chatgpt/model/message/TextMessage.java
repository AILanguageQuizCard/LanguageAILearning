package com.chunxia.chatgpt.model.message;

import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;
import com.google.gson.annotations.Expose;

public class TextMessage extends Message {
    @Expose
    private String content;

    private Text2VoiceModel text2VoiceModel;

    public int voiceStatus;

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

    public Text2VoiceModel getText2VoiceModel() {
        return text2VoiceModel;
    }

    public void setText2VoiceModel(Text2VoiceModel text2VoiceModel) {
        this.text2VoiceModel = text2VoiceModel;
    }


    public int getVoiceStatus() {
        return voiceStatus;
    }

    public void setVoiceStatus(int voiceStatus) {
        this.voiceStatus = voiceStatus;
    }
}
