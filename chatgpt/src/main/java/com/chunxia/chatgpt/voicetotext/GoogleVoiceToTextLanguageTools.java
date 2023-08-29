package com.chunxia.chatgpt.voicetotext;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.chunxia.chatgpt.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleVoiceToTextLanguageTools {

    private static ArrayList<GoogleVoiceToTextLanguage> data = null;

    public static class GoogleVoiceToTextLanguage {
        private String code;
        private String languageName;
        private String level = "Standard";

        public GoogleVoiceToTextLanguage(String code, String languageName) {
            this.code = code;
            this.languageName = languageName;
        }

        public GoogleVoiceToTextLanguage(String code, String languageName, String level) {
            this.code = code;
            this.languageName = languageName;
            this.level = level;
        }


        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLanguageName() {
            return languageName;
        }

        public void setLanguageName(String languageName) {
            this.languageName = languageName;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static ArrayList<GoogleVoiceToTextLanguage> getLanguages(Context context) {
        if (data!=null) return data;
        // 在你的Activity或Fragment中
        ArrayList<GoogleVoiceToTextLanguage> arrayList =  new ArrayList<>();
        Resources res = context.getResources();
        String[] googleLanguages = res.getStringArray(R.array.google_api_recording_voice_language_names);

        for (String googleLanguage : googleLanguages) {
            String[] split = googleLanguage.split(";");
            String languageName = split[0];
            String code = split[1];
            String level = split[2];
            Log.i("GoogleVoiceToTextLanguageTools", languageName + ": " + code);
            arrayList.add(new GoogleVoiceToTextLanguage(code, languageName, level));
        }
        data = arrayList;
        return arrayList;
    }

    public static String getLanguageCodeByLanguageName(Context context, String languageName){
        if (data == null) getLanguages(context);
        List<GoogleVoiceToTextLanguage> collect = data.stream()
                .filter(item -> item.languageName.equals(languageName)).collect(Collectors.toList());
        if (collect.size() == 0) return "en-US";
        else {
            return collect.get(0).code;
        }
    }


}
