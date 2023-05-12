package com.chunxia.chatgpt.chatapi;

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
