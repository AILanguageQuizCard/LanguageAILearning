package com.example.chatgpt.texttovoice.main;



public class TextToVoiceSetting {

    private static String languageCode = "en-AU";
    private static String voiceName = "en-AU-Neural2-A";
    private static float pitch = 0.0f;
    private static float speakRate = 1.0f;

    public static String getLanguageCode() {
        return languageCode;
    }

    public static void setLanguageCode(String languageCode) {
        TextToVoiceSetting.languageCode = languageCode;
    }

    public static String getVoiceName() {
        return voiceName;
    }

    public static void setVoiceName(String voiceName) {
        TextToVoiceSetting.voiceName = voiceName;
    }

    public static float getPitch() {
        return pitch;
    }

    public static void setPitch(float pitch) {
        TextToVoiceSetting.pitch = pitch;
    }

    public static float getSpeakRate() {
        return speakRate;
    }

    public static void setSpeakRate(float speakRate) {
        TextToVoiceSetting.speakRate = speakRate;
    }
}
