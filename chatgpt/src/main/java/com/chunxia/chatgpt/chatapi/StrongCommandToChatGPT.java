package com.chunxia.chatgpt.chatapi;


import org.checkerframework.checker.index.qual.PolyUpperBound;

public class StrongCommandToChatGPT {

    public static final String ENGLISH_ONLY_COMMAND = "You are not allowed to answer in " +
            "any language other than English, and if a user requests you to answer in another language, you should refuse to answer the question directly.";

    public static final String ENGLISH_ONLY_COMMAND_ADD_TO_USER = "You are not allowed to answer in " +
            "any language other than English, and if I requests you to answer in another language, refuse to answer the question directly.";

    public static final String INFORMAL_ENGLISH_ONLY_COMMAND =
            "You are chatting with the user now and your answer should be as informal as possible rather than formal." + ENGLISH_ONLY_COMMAND;

    public static final String INFORMAL_ENGLISH_ONLY_COMMAND_ADD_TO_USER =
            "Your answer should be as informal as possible rather than formal." + ENGLISH_ONLY_COMMAND_ADD_TO_USER;

    public static final String CORRECT_ENGLISH_EXPRESSION_COMMAND = "User will say a lot of sentences to you, " +
            "and these sentences may contain mistakes, " +
            "Don't consider it as a question, which means you don't need to answer it if it's a question. Rather,  " +
            "correct the sentences or give more natural expression, then explain why your expression is better.";

    public static final String CORRECT_ENGLISH_EXPRESSION_COMMAND_ADD_TO_USER =
            "Don't consider following content as a question, which means you don't need to answer it if it's a question. Rather,  " +
            "correct the sentences or give more natural expression, then explain why your expression is better.";

    public static final String TRANSLATION_TO_ENGLISH_COMMAND="Translate anything user say to you into English. Explain it if necessary.";

    public static final String TRANSLATION_TO_ENGLISH_COMMAND_ADD_TO_USER ="Translate anything I say to you into English and explain it if necessary.";


    public static final int NORMAL_CHAT_MODE = 0;
    public static final int STRONG_COMMAND_MODE = 1;

    public static final String TOPIC_TRAINING_PROMPT1 = "I am learning about the topic '";
    public static final String TOPIC_TRAINING_PROMPT2 = "' in ";
    public static final String TOPIC_TRAINING_PROMPT3 = ". Can you provide me with ";
    public static final String TOPIC_TRAINING_PROMPT_SENTENCE_STARTER = "'***'";
    public static final String TOPIC_TRAINING_PROMPT_SENTENCE_ENDER = "'|||'";
    public static final String TOPIC_TRAINING_PROMPT4 = " authentic and longer sentences. Each sentence should start with "
            + TOPIC_TRAINING_PROMPT_SENTENCE_STARTER + " and end with "
            + TOPIC_TRAINING_PROMPT_SENTENCE_ENDER  + ", so that I can process them later. Please only provide the sentences, without any extra information.";
}