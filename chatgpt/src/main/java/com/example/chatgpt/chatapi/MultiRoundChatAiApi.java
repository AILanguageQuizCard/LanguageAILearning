package com.example.chatgpt.chatapi;

import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.ENGLISH_ONLY_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.ENGLISH_ONLY_MODE;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.INFORMAL_ENGLISH_ONLY_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.INFORMAL_ENGLISH_ONLY_MODE;


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
import java.util.stream.Collectors;

public class MultiRoundChatAiApi {

    private static String TAG = "MultiRoundChatAiApi";
    private List<ChatMessage> oldMessages = new ArrayList<>();
    private ChatMessage systemMessage;
    private final List<ThreadUtils.Task<String>> threadTasks = new ArrayList<>();
    private int mode = 0;

    public MultiRoundChatAiApi(String systemCommand, int mode) {
        init(systemCommand, mode);
    }

    private void init(String systemCommand, int mode) {
        this.mode = mode;
        systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), systemCommand);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            threadTasks.forEach(ThreadUtils::cancel);
        }
    }


    public static interface ReceiveOpenAiReply{
        public void onSuccess(String reply);
    }

    private boolean isNotSystemMessage(ChatMessage chatMessage) {
        return chatMessage.getRole() != ChatMessageRole.SYSTEM.value();
    }

    private void insertStrongCommand() {

        if (this.mode == ENGLISH_ONLY_MODE) {
            // 只允许gpt说英文
            oldMessages = oldMessages.stream().filter(this::isNotSystemMessage).collect(Collectors.toList());
            final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), ENGLISH_ONLY_COMMAND);
            oldMessages.add(systemMessage);
        } else if (this.mode == INFORMAL_ENGLISH_ONLY_MODE) {
            // 只允许gpt说英文 + 口语对话
            oldMessages = oldMessages.stream().filter(this::isNotSystemMessage).collect(Collectors.toList());
            final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), INFORMAL_ENGLISH_ONLY_COMMAND);
            oldMessages.add(systemMessage);
        }

    }

    public String sendToChatAi(String message) {
        System.out.println("User: " + message);
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        oldMessages.add(userMessage);

        insertStrongCommand();
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


    public void clearOldMessage() {
        oldMessages = new ArrayList<>();
        oldMessages.add(systemMessage);
    }

    public void addChatGptReplyToMessage(ChatMessage message) {
        oldMessages.add(message);
    }


}
