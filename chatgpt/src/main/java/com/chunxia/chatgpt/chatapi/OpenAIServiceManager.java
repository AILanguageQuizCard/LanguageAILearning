package com.chunxia.chatgpt.chatapi;

//import com.chunxia.firebase.RealtimeDatabase;
import com.chunxia.firebase.config.RemoteConfig;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;

public class OpenAIServiceManager {
    public static OpenAiService getOpenAiService(){
        OpenAiService service1;
        // todo 这里只支持O以上版本！
        String token = RemoteConfig.getInstance().getOpenAIApiKey();
        if (token != null) {
            service1 = new OpenAiService(token, Duration.ofSeconds(120));
            return service1;
        } else {
            return null;
        }
    }


}
