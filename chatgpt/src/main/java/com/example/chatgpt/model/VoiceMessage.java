package com.example.chatgpt.model;

public class VoiceMessage extends Message{



    public VoiceMessage(long id, boolean fromMe, String date) {
        super(id, fromMe, date);
    }

    public VoiceMessage(long id, boolean fromMe, boolean showTime, String date) {
        super(id, fromMe, showTime, date);
    }

    @Override
    public boolean isVoice() {
        return true;
    }
}
