package com.example.chatgpt.chatapi;

import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.STRONG_COMMAND_MODE;


import android.os.Build;
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
    private final List<ChatMessage> backupMessages = new ArrayList<>();

    private ChatMessage systemMessage;
    private String systemCommand;
    private String beforeUserMessageCommand;
    private final List<ThreadUtils.Task<String>> threadTasks = new ArrayList<>();
    private int mode = 0;

    public MultiRoundChatAiApi(String systemCommand, String beforeUserMessageCommand, int mode) {
        init(systemCommand, beforeUserMessageCommand, mode);
    }

    private void init(String systemCommand, String beforeUserMessageCommand, int mode) {
        this.mode = mode;
        this.systemCommand = systemCommand;
        this.beforeUserMessageCommand = beforeUserMessageCommand;
        systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.systemCommand);
        oldMessages.add(systemMessage);
        backupMessages.add(systemMessage);
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
        if (this.mode == STRONG_COMMAND_MODE) {
            oldMessages = backupMessages;
            String newMessage = beforeUserMessageCommand + " " + message;
            final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), newMessage);
            backupMessages(message);
            oldMessages.add(userMessage);
        }
    }

    private void backupMessages(String message) {
        backupMessages.clear();
        backupMessages.addAll(oldMessages);
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        backupMessages.add(userMessage);

    }


    public String sendToChatAi(String message) {
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
        backupMessages.add(message);
    }


}
