package com.chunxia.chatgpt.chatapi;

import android.os.Build;

import com.chunxia.firebase.RealtimeDatabase;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;

public class OpenAIServiceManager {
    private volatile static String token = "";
    public static OpenAiService service;
    public static OpenAiService getOpenAiService(){
        OpenAiService service1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // todo 这里只支持O以上版本！
            service1 = new OpenAiService(token, Duration.ofSeconds(120));
            // todo 这里因为token报错应该往外抛
            return service1;
        } else {
            return null;
        }
    }


    public static void initApiKey() {
        RealtimeDatabase.getInstance().getChatGptApiKeyOnce(new RealtimeDatabase.onRealtimeDatabaseListener() {
            @Override
            public void onDataChange(String apiKey) {
                setApiKey(apiKey);
            }
        });
    }


    public static void setApiKey(String apiKey) {
        token = apiKey;
    }


}
