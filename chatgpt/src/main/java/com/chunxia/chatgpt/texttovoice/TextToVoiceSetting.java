package com.chunxia.chatgpt.texttovoice;


import android.app.Application;
import android.content.Context;

public class TextToVoiceSetting {

    private static String languageCode = "en-AU";
    private static String voiceName = "en-AU-Neural2-A";
    private static float pitch = 0.0f;
    private static float speakRate = 1.0f;

    public static String getLanguageCode() {
        return languageCode;
    }

    public static String getLanguageCode(Context context, String languageName) {
        languageCode = GoogleTextToVoiceLanguageTools.getLanguageCodeByLanguageName(context, languageName);
        return languageCode;
    }

    public static void setLanguageCode(String languageCode) {
        TextToVoiceSetting.languageCode = languageCode;
    }

    public static String getVoiceName() {
        return voiceName;
    }

    public static String getVoiceName(Context context, String languageName) {
        voiceName = GoogleTextToVoiceLanguageTools.getNameByLanguageName(context, languageName);
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
