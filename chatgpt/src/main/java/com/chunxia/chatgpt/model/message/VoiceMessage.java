package com.chunxia.chatgpt.model.message;

import com.chunxia.chatgpt.model.message.Message;

public class VoiceMessage extends Message {

    private String path =null;

    public VoiceMessage(long id, boolean fromMe, boolean showTime, String date, String path) {
        super(id, fromMe, showTime, date);
        this.path = path;
        messageType = "VoiceMessage";
    }

    @Override
    public boolean isVoice() {
        return true;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
