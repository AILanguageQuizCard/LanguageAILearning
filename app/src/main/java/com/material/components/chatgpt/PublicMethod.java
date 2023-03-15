package com.material.components.chatgpt;

import com.theokanning.openai.service.OpenAiService;

public class PublicMethod {
    private static final String token = "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";
    public static OpenAiService service;
    public static OpenAiService getOpenAiService(){
        if (service == null) {
            service = new OpenAiService(token);
        }
        return service;
    }

}
