package com.chunxia.chatgpt.chatapi;

import android.util.Log;

import com.blankj.utilcode.util.ThreadUtils;
import com.chunxia.chatgpt.model.review.SentenceCard;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.ArrayList;
import java.util.List;

public class MultiRoundChatAgent {

    private static final String TAG = "MultiRoundChatAiApi";
    private final List<ChatMessage> oldMessages = new ArrayList<>();

    private String model = "gpt-3.5-turbo";
    private int responseN = 1;
    private int maxTokenN = 512;
    private final ChatMessage systemMessage;
    private final String systemCommand;
    private final List<ThreadUtils.Task<String>> threadTasks = new ArrayList<>();

    public MultiRoundChatAgent(String systemCommand, String model, int responseN, int maxTokenN) {
        this.systemCommand = systemCommand;
        this.model = model;
        this.responseN = responseN;
        this.maxTokenN = maxTokenN;
        this.systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.systemCommand);
        oldMessages.add(systemMessage);
    }



    public MultiRoundChatAgent() {
        this.systemCommand = "";
        this.systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.systemCommand);
        oldMessages.add(systemMessage);
    }

    public MultiRoundChatAgent(String systemCommand) {
        this.systemCommand = systemCommand;
        this.systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), this.systemCommand);
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

        ThreadUtils.getIoPool().execute(tTask);
    }

    public String sendMessage(String message) {
        return sendToChatAi(message);
    }

    public void cancelAllCurrentThread() {
        // todo 只取消当前正在执行的
        threadTasks.forEach(ThreadUtils::cancel);
    }

    public SentenceCard getOneRoundSentenceCard() {
        if (oldMessages.size() < 3) {
            return null;
        }
        SentenceCard sentenceCard = new SentenceCard(oldMessages.get(2).getContent(),oldMessages.get(1).getContent());
        return sentenceCard;
    }


    public interface ReceiveOpenAiReply {
        void onSuccess(String reply);
    }

    private void insertUserMessage(String message) {
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        oldMessages.add(userMessage);
    }

    private String sendToChatAi(String message) {
        Log.i(TAG, "User: " + message);
        insertUserMessage(message);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(model)
                .messages(oldMessages)
                .n(responseN)
                .maxTokens(maxTokenN)
                .build();

        List<ChatCompletionChoice> choices = OpenAIServiceManager.getOpenAiService()
                .createChatCompletion(chatCompletionRequest).getChoices();
        if (!choices.isEmpty()) {
            String content = choices.get(0).getMessage().getContent();
            Log.i(TAG, "ChatGpt: " + content);
            addChatGptReplyToMessage(choices.get(0).getMessage());
            return content;
        }

        return null;
    }


    public void clearOldMessage() {
        oldMessages.clear();
        oldMessages.add(systemMessage);
    }

    public void addChatGptReplyToMessage(ChatMessage message) {
        oldMessages.add(message);
    }

    public int getMaxTokenN() {
        return maxTokenN;
    }

    public void setMaxTokenN(int maxTokenN) {
        this.maxTokenN = maxTokenN;
    }
}
