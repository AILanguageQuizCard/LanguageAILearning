package com.chunxia.chatgpt.chatapi;

import android.util.Log;

import com.blankj.utilcode.util.ThreadUtils;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiRoundChatAiApi {

    private static final String TAG = "MultiRoundChatAiApi";
    private List<ChatMessage> oldMessages = new ArrayList<>();

    private ChatMessage systemMessage;
    private String systemCommand;
    private final List<ThreadUtils.Task<String>> threadTasks = new ArrayList<>();
    private int mode = 0;

    public MultiRoundChatAiApi(String systemCommand, int mode) {
        init(systemCommand, mode);
    }

    private void init(String systemCommand, int mode) {
        this.mode = mode;
        this.systemCommand = systemCommand;
        systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.systemCommand);
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
                Log.i(TAG, "receive reply from chatgpt");
                onReceiveOpenAiReply.onSuccess(result);
            }
        };
        threadTasks.add(tTask);
        ThreadUtils.executeBySingle(tTask);
    }

    public void cancelAllCurrentThread() {
        // todo 只取消当前正在执行的
        threadTasks.forEach(ThreadUtils::cancel);
    }


    public interface ReceiveOpenAiReply{
        void onSuccess(String reply);
    }


    private void insertUserMessage(String message) {
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        oldMessages.add(userMessage);
    }


    private String sendToChatAi(String message) {
        System.out.println("User: " + message);
        insertUserMessage(message);

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
            choices.forEach(chatChoice -> {
                System.out.println("ChatGpt: " + chatChoice.getMessage().getContent());
            });
            return choices.get(0).getMessage().getContent();
        }

        return null;
    }


    public void clearOldMessage() {
        oldMessages = new ArrayList<>();
        oldMessages.add(systemMessage);
    }

    public void addChatGptReplyToMessage(ChatMessage message) {
        oldMessages.add(message);
    }


}
