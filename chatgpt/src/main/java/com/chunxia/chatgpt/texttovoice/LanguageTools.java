package com.chunxia.chatgpt.texttovoice;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.chunxia.chatgpt.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class LanguageTools {

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


    private static HashMap<String, GoogleTextToVoiceLanguage> map = new HashMap<>();
    public static GoogleTextToVoiceLanguage getGoogleTextToVoiceLanguage(Context context, String languageInMotherLanguage ) {
        initLanguageMap(context);
        return map.get(languageInMotherLanguage);
    }


    private static void initLanguageMap(Context context) {
        if (map.size() == 0) {
            Resources res = context.getResources();
            String[] learningLanguages = res.getStringArray(R.array.learning_language_list);
            String[] googleTTVLanguages = res.getStringArray(R.array.google_api_voice_language_names);

            HashMap<String, GoogleTextToVoiceLanguage> tempMap = new HashMap<>();

            for (String googleLanguage : googleTTVLanguages) {
                String[] split = googleLanguage.split(";");
                String languageName = split[0];
                String level = split[1];
                String code = split[2];
                String name = split[3];
                String sex = split[4];
                tempMap.put(languageName, new GoogleTextToVoiceLanguage(code, name, languageName, sex, level));
            }

            HashMap<String, GoogleTextToVoiceLanguage> finalMap = new HashMap<>();

            for (String lang: learningLanguages) {
                String[] split = lang.split(";");
                String languageNameInMotherLanguage = split[0];
                String languageNameInOriginalLanguage = split[1];
                finalMap.put(languageNameInMotherLanguage, tempMap.get(languageNameInOriginalLanguage));
            }
            map = finalMap;
        }
    }


    private static HashMap<String, LearningLanguage> languageInML2LanguageInEnglish = new HashMap<>();

    private static ArrayList<LearningLanguage> learningLanguageArrayList = new ArrayList<>();
    public static String getEnglishNameForLearningLanguageName(Context context, String languageNameInMotherLanguage) {
        initLearningLanguages(context);
        String result = Objects.requireNonNull(languageInML2LanguageInEnglish.get(languageNameInMotherLanguage)).languageNameInEnglish;
        if ( result.isEmpty()){
            return "English";
        } else {
            return result;
        }
    }

    public static ArrayList<LearningLanguage> getLearningLanguageList(Context context) {
        initLearningLanguages(context);
        return learningLanguageArrayList;
    }

    @NonNull
    private static void initLearningLanguages(Context context) {
        if (languageInML2LanguageInEnglish.size() == 0 || learningLanguageArrayList.isEmpty()) {
            Resources res = context.getResources();
            String[] learningLanguages = res.getStringArray(R.array.learning_language_list);

            for (String lang : learningLanguages) {
                String[] split = lang.split(";");
                String languageName = split[0];
                String googleTextToVoiceLanguageName = split[1];
                String languageNameInEnglish = split[2];
                LearningLanguage learningLanguage = new LearningLanguage(languageName, languageNameInEnglish, googleTextToVoiceLanguageName);
                learningLanguageArrayList.add(learningLanguage);
                languageInML2LanguageInEnglish.put(languageName, learningLanguage);
            }
        }
    }


    public static class MotherLanguage{
        String languageNameInMotherLanguage;
        String languageNameInEnglish;

        public MotherLanguage(String languageNameInMotherLanguage, String languageNameInEnglish) {
            this.languageNameInMotherLanguage = languageNameInMotherLanguage;
            this.languageNameInEnglish = languageNameInEnglish;
        }

        public String getLanguageNameInMotherLanguage() {
            return languageNameInMotherLanguage;
        }

        public void setLanguageNameInMotherLanguage(String languageNameInMotherLanguage) {
            this.languageNameInMotherLanguage = languageNameInMotherLanguage;
        }

        public String getLanguageNameInEnglish() {
            return languageNameInEnglish;
        }

        public void setLanguageNameInEnglish(String languageNameInEnglish) {
            this.languageNameInEnglish = languageNameInEnglish;
        }
    }

    private static HashMap<String, MotherLanguage> motherLanguageHashMap = new HashMap<>();
    private static ArrayList< MotherLanguage> motherLanguageArrayList = new ArrayList<>();
    public static String getEnglishNameForMotherLanguage(Context context, String languageNameInMotherLanguage) {
        initMotherLanguages(context);
        String result = Objects.requireNonNull(motherLanguageHashMap.get(languageNameInMotherLanguage)).languageNameInEnglish;
        if ( result.isEmpty()){
            return "English";
        } else {
            return result;
        }
    }


    public static ArrayList<MotherLanguage> getMotherLanguageArrayList(Context context) {
        initMotherLanguages(context);
        return motherLanguageArrayList;
    }

    @NonNull
    private static void initMotherLanguages(Context context) {
        if (motherLanguageHashMap.size() == 0 || motherLanguageArrayList.isEmpty()) {
            Resources res = context.getResources();
            String[] learningLanguages = res.getStringArray(R.array.mother_language_list);

            for (String lang : learningLanguages) {
                String[] split = lang.split(";");
                String languageName = split[0];
                String motherLanguageInEnglish = split[1];
                MotherLanguage motherLanguage = new MotherLanguage(languageName, motherLanguageInEnglish);
                motherLanguageArrayList.add(motherLanguage);
                motherLanguageHashMap.put(languageName, motherLanguage);
            }
        }
    }


    public static class LearningLanguage {
        String languageNameInMotherLanguage;

        String languageNameInEnglish;

        String googleTextToVoiceLanguageName;

        public LearningLanguage(String languageNameInMotherLanguage, String languageNameInEnglish, String googleTextToVoiceLanguageName) {
            this.languageNameInMotherLanguage = languageNameInMotherLanguage;
            this.languageNameInEnglish = languageNameInEnglish;
            this.googleTextToVoiceLanguageName = googleTextToVoiceLanguageName;
        }

        public String getLanguageNameInMotherLanguage() {
            return languageNameInMotherLanguage;
        }

        public void setLanguageNameInMotherLanguage(String languageNameInMotherLanguage) {
            this.languageNameInMotherLanguage = languageNameInMotherLanguage;
        }
    }


    public static String getLanguageCodeByLanguageName(Context context, String languageName){
        if (map.size() == 0) initLanguageMap(context);
        String defaultV = "en-AU";
        if (map.get(languageName) == null) return defaultV;
        GoogleTextToVoiceLanguage googleTextToVoiceLanguage = Objects.requireNonNull(map.get(languageName));
        return googleTextToVoiceLanguage.code;
    }

    public static String getNameByLanguageName(Context context, String languageName){
        if (map.size() == 0) initLanguageMap(context);
        String defaultV = "en-AU-Standard-A";
        if (map.get(languageName) == null) return defaultV;
        GoogleTextToVoiceLanguage googleTextToVoiceLanguage = Objects.requireNonNull(map.get(languageName));
        return googleTextToVoiceLanguage.name;
    }


}
