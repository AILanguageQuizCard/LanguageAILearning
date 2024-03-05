package com.chunxia.chatgpt.voicetotext;

public class VoiceToTextSetting {
    private static String languageName;
    private static String languageCode;

    public static String getLanguageName() {
        return languageName;
    }

    public static void setLanguageName(String languageName) {
        VoiceToTextSetting.languageName = languageName;
    }

    public static String getLanguageCode() {
        return languageCode;
    }

    public static void setLanguageCode(String languageCode) {
        VoiceToTextSetting.languageCode = languageCode;
    }
}
