package com.chunxia.chatgpt.chatapi;

import android.os.Build;

import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;

public class PublicMethod {
    private static final String token = "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";
    public static OpenAiService service;
    public static OpenAiService getOpenAiService(){
        OpenAiService service1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // todo 这里只支持O以上版本！
            service1 = new OpenAiService(token, Duration.ofSeconds(120));
            return service1;
        } else {
            return null;
        }

    }

}
