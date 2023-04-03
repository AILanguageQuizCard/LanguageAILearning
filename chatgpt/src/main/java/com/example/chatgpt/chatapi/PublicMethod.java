package com.example.chatgpt.chatapi;

import android.os.Build;

import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;

public class PublicMethod {
    private static final String token = "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";
    public static OpenAiService service;
    public static OpenAiService getOpenAiService(){
        if (service == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                service = new OpenAiService(token, Duration.ofSeconds(60));
            }
        }
        return service;
    }
}
