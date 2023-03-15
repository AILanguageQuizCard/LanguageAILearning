package com.material.components.chatgpt;

import android.os.Build;

import com.blankj.utilcode.util.ThreadUtils;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiRoundChatAiApi {
    private final List<ChatMessage> oldMessages = new ArrayList<>();;

    public MultiRoundChatAiApi() {
        init();
    }

    private void init() {
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "do as what the user ask you to do");
        oldMessages.add(systemMessage);
    }


    public void sendMessageInThread(String message, ReceiveOpenAiReply onReceiveOpenAiReply) {
        ThreadUtils.Task<String> tTask = new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() throws Throwable {
                return sendToChatAi(message);
            }

            @Override
            public void onSuccess(String result) {
                onReceiveOpenAiReply.onSuccess(result);
            }
        };
        ThreadUtils.executeBySingle(tTask);
    }


    public static interface ReceiveOpenAiReply{
        public void onSuccess(String reply);
    }

    public String sendToChatAi(String message) {
        System.out.println("User: " + message);
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        oldMessages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(oldMessages)
                .n(1)
                .maxTokens(512)
                .logitBias(new HashMap<>())
                .build();

        List<ChatCompletionChoice> choices = PublicMethod.getOpenAiService()
                .createChatCompletion(chatCompletionRequest).getChoices();
        if (choices.size() > 0) {
            addChatGptReplyToMessage(choices.get(0).getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                choices.forEach(chatChoice -> {
                    System.out.println("ChatGpt: " + chatChoice.getMessage().getContent());
                });
            }
            return choices.get(0).getMessage().getContent();
        }

        return null;
    }



    public void addChatGptReplyToMessage(ChatMessage message) {
        oldMessages.add(message);
    }


}
