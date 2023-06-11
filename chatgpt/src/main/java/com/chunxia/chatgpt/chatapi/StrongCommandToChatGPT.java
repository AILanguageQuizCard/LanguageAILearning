package com.chunxia.chatgpt.chatapi;

import android.util.Log;

import java.util.Locale;

public class StrongCommandToChatGPT {

    private static final String TAG = "StrongCommandToChatGPT";

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


    public static final String TOPIC_TRAINING_PROMPT =
            "I am learning about the topic '%s' in %s. Can you provide me with %d authentic sentences and its %s translation. For every pair of sentence and its translation, start with its count number and end with line break. And using %s %s to label translation. One example: \"1. sentence in %s. [the corresponding %s translation]\". I need to process your response using code, so your reply should only include the answer and should not include any additional introductory text, otherwise it may cause error to my program. ";
    public  static final String LEFT_KUOHAO = "[";
    public  static final String RIGHT_KUOHAO = "]";

    public static String getCompleteTopicTrainingPrompt(String topic, String language, String motherLanguage, int num) {
        String result =  String.format(Locale.getDefault(), TOPIC_TRAINING_PROMPT,
                topic, language, num, motherLanguage,
                LEFT_KUOHAO, RIGHT_KUOHAO, language, motherLanguage);
        Log.i(TAG, result);
        return result;
    }

    public static final String TOPIC_TRAINING_QUESTION_PROMPT =
            "I am learning about the topic '%s' in %s. Can you provide me with %d oral practice questions for students on the topic and your answer of these questions. For every pair of question and its answer, start with its count number and end with line break. And using %s %s to label answer. One example: \"1. question [your answer]\". I need to process your response using code, so your reply should only include the answer and should not include any additional introductory text, otherwise it may cause error to my program.";

    public static String getCompleteTopicTrainingQuestionPrompt(String topic, String language, String motherLanguage, int num) {
        String result =  String.format(Locale.getDefault(), TOPIC_TRAINING_QUESTION_PROMPT, topic, language, num, LEFT_KUOHAO, RIGHT_KUOHAO);
        Log.i(TAG, result);
        return result;
    }


    public static final String SENTENCE_PATTERN_TRAINING_PROMPT =
            "I am learning about the sentence pattern '%s' in %s. Can you provide me with %d authentic sentences of that sentence structure and its %s translation. For every pair of sentence and its translation, start with its count number and end with line break. And using %s %s to label translation. One example: \"1. one sentence of that sentence pattern in %s. [the corresponding %s translation]\". I need to process your response using code, so your reply should only include the answer and should not include any additional introductory text, otherwise it may cause error to my program. ";


    public static String getSentencePatternExamplesPrompt(String sentencePatter, String language, String motherLanguage, int num) {
        String result =  String.format(Locale.getDefault(), SENTENCE_PATTERN_TRAINING_PROMPT,
                sentencePatter, language, num, motherLanguage,
                LEFT_KUOHAO, RIGHT_KUOHAO, language, motherLanguage);
        Log.i(TAG, result);
        return result;
    }

    public static final String GRAMMAR_TRAINING_PROMPT =
            "I am learning about the grammar '%s' in %s. Can you provide me with %d authentic examples sentences and its %s translation. For every pair of sentence and its translation, start with its count number and end with line break. And using %s %s to label translation. One example: \"1. one sentence of that grammar in %s. [the corresponding %s translation]\". I need to process your response using code, so your reply should only include the answer and should not include any additional introductory text, otherwise it may cause error to my program. ";

    public static String getGrammarExamplesPrompt(String grammar, String language, String motherLanguage, int num) {
        String result =  String.format(Locale.getDefault(), GRAMMAR_TRAINING_PROMPT,
                grammar, language, num, motherLanguage,
                LEFT_KUOHAO, RIGHT_KUOHAO, language, motherLanguage);
        Log.i(TAG, result);
        return result;
    }



    public static String getOpinionPrompt(String opinion, String language) {
        String prompt =  "I hold the viewpoint that \"%s\" I want to express my opinion in %s. Please argue my viewpoint in %s, not exceeding 100 words.";
        String result =  String.format(Locale.getDefault(), prompt, opinion, language, language);
        Log.i(TAG, result);
        return result;
    }


}

