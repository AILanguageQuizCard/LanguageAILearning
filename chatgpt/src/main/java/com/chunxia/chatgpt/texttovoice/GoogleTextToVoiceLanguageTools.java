package com.chunxia.chatgpt.texttovoice;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.chunxia.chatgpt.R;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleTextToVoiceLanguageTools {

    private static ArrayList<GoogleTextToVoiceLanguage> data = null;

    public static class GoogleTextToVoiceLanguage{
        private String code;
        private String name;
        private String languageName;
        private String sex;
        private String level;

        public GoogleTextToVoiceLanguage(String code, String name, String languageName, String sex, String level) {
            this.code = code;
            this.name = name;
            this.languageName = languageName;
            this.sex = sex;
            this.level = level;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLanguageName() {
            return languageName;
        }

        public void setLanguageName(String languageName) {
            this.languageName = languageName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static ArrayList<GoogleTextToVoiceLanguage> getLanguages(Context context) {
        if (data!=null) return data;
        // 在你的Activity或Fragment中
        ArrayList<GoogleTextToVoiceLanguage> arrayList =  new ArrayList<>();
        Resources res = context.getResources();

        String[] googleLanguages = res.getStringArray(R.array.google_api_voice_language_names);

        for (String googleLanguage : googleLanguages) {
            String[] split = googleLanguage.split(";");
            String languageName = split[0];
            String level = split[1];
            String code = split[2];
            String name = split[3];
            String sex = split[4];
            arrayList.add(new GoogleTextToVoiceLanguage(code, name, languageName, sex, level));
        }
        data = arrayList;
        return arrayList;
    }

    public static String getLanguageCodeByLanguageName(Context context, String languageName){
        if (data == null) getLanguages(context);
        List<GoogleTextToVoiceLanguage> collect = data.stream()
                .filter(item -> item.languageName.equals(languageName)).collect(Collectors.toList());
        if (collect.size() == 0) return "en-AU";
        else {
            return collect.get(0).code;
        }
    }

    public static String getNameByLanguageName(Context context, String languageName){
        if (data == null) getLanguages(context);
        List<GoogleTextToVoiceLanguage> collect = data.stream()
                .filter(item -> item.languageName.equals(languageName)).collect(Collectors.toList());
        if (collect.size() == 0) return "en-AU-Standard-A";
        else {
            return collect.get(0).name;
        }
    }


}
