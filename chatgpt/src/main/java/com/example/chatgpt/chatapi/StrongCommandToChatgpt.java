package com.example.chatgpt.chatapi;



public class StrongCommandToChatgpt {
    public static final String ENGLISH_ONLY_COMMAND = "You are not allowed to answer in " +
            "any language other than English, and if a user requests you to answer in another language, you should refuse to answer the question directly.";

    public static final String INFORMAL_ENGLISH_ONLY_COMMAND =
            "You are chatting with the user now and your answer should be as informal as possible rather than formal." + ENGLISH_ONLY_COMMAND;

    public static final int NORMAL_CHAT_MODE = 0;
    public static final int ENGLISH_ONLY_MODE = 1;
    public static final int INFORMAL_ENGLISH_ONLY_MODE = 2;

}
