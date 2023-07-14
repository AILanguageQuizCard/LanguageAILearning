package com.chunxia.chatgpt.chatapi;

import android.util.Log;
import android.util.Pair;

import com.chunxia.chatgpt.model.review.SentenceCard;
import com.chunxia.chatgpt.model.review.TopicTestCard;

import java.util.ArrayList;

public class ChatGptResponseTools {

    private static String TAG = "ChatGptResponseTools";

    public static ArrayList<SentenceCard> extractTopicTrainingSentences(String input, int targetN) throws ExtractSentencesException {
        ArrayList<SentenceCard> sentenceCards = new ArrayList<>();

        int sentenceCount = 1;
        String[] words = extractAllSentences(input, targetN);
        for (String word : words) {
            Log.i(TAG, "sentence: " + word);
            if (word.isEmpty()) continue;
            String deleteS = (sentenceCount + ".");
            String res = word.replace(deleteS, "").trim();
            Pair<String, String> pair = extractOneSentence(res);
            Log.i(TAG, "sentence: " + pair.first + "\ntranslation: " + pair.second);
            sentenceCards.add(new SentenceCard(pair.first, pair.second));
            sentenceCount++;
        }

        return sentenceCards;
    }


    public static class ExtractSentencesException extends Exception  {
        public ExtractSentencesException(String message) {
            super(message);
        }
    }



    public static ArrayList<TopicTestCard> extractTopicTrainingQuestions(String input, int targetN) throws ExtractSentencesException {
        ArrayList<TopicTestCard> topicTestCards = new ArrayList<>();

        int sentenceCount = 1;
        String[] words = extractAllSentences(input, targetN);
        for (String word : words) {
            if (word.isEmpty()) continue;
            String deleteS = (sentenceCount + ".");
            String res = word.replace(deleteS, "").trim();
            Pair<String, String> pair = extractOneSentence(res);
            topicTestCards.add(new TopicTestCard(pair.first, pair.second));
            sentenceCount++;
        }

        return topicTestCards;
    }

    private static String[] extractAllSentences(String input, int targetN) throws ExtractSentencesException {
        int finalStartIndex = 0;
        int realSentencesStartIndex = input.indexOf("\n1. ");
        int realSentencesStartIndex2 = input.indexOf("1. ");
        int realSentencesStartIndex3 = input.indexOf("1 ");
        int realSentencesStartIndex4 = input.indexOf("\n1.");
        int realSentencesStartIndex5 = input.indexOf("1.");
        int realSentencesStartIndex6 = input.indexOf("1");

        if (realSentencesStartIndex != -1) {
            finalStartIndex = realSentencesStartIndex;
        } else if (realSentencesStartIndex2 != -1) {
            finalStartIndex = realSentencesStartIndex2;
        } else if (realSentencesStartIndex3 != -1) {
            finalStartIndex = realSentencesStartIndex3;
        } else if (realSentencesStartIndex4 != -1) {
            finalStartIndex = realSentencesStartIndex4;
        } else if (realSentencesStartIndex5 != -1) {
            finalStartIndex = realSentencesStartIndex5;
        } else if (realSentencesStartIndex6 != -1) {
            finalStartIndex = realSentencesStartIndex6;
        } else {
            // if none of the start index found, it means parsing failed.
           throw new ExtractSentencesException("extractAllSentences failed");
        }

        String newInput = input.substring(finalStartIndex);
        String[] sentences = newInput.split("\n");

        if (sentences.length != targetN) {
            throw new ExtractSentencesException("extractAllSentences failed");
        }
        // Copy the sentences to the resultList.

        return sentences;
    }


    private static Pair<String, String> extractOneSentence(String res) throws ExtractSentencesException{
        String sentence = "";
        String translation = "";
        int finalKuoHaoIndex = 0;
        int leftKuohaoIndex = res.indexOf("." + StrongCommandToChatGPT.LEFT_KUOHAO);
        int leftKuohaoIndex2 = res.indexOf(". " + StrongCommandToChatGPT.LEFT_KUOHAO);
        int leftKuohaoIndex3 = res.indexOf(StrongCommandToChatGPT.LEFT_KUOHAO);
        if (leftKuohaoIndex != -1) {
            finalKuoHaoIndex = leftKuohaoIndex;
            sentence= res.substring(0, finalKuoHaoIndex+1);
            translation = res.substring(finalKuoHaoIndex+2)
                    .replace(StrongCommandToChatGPT.RIGHT_KUOHAO, "")
                    .trim();
        } else if (leftKuohaoIndex2 != -1) {
            finalKuoHaoIndex = leftKuohaoIndex2;
            sentence= res.substring(0, finalKuoHaoIndex+1);
            translation = res.substring(finalKuoHaoIndex+3)
                    .replace(StrongCommandToChatGPT.RIGHT_KUOHAO, "")
                    .trim();
        } else if (leftKuohaoIndex3 != -1) {
            finalKuoHaoIndex = leftKuohaoIndex3;
            sentence= res.substring(0, finalKuoHaoIndex);
            translation = res.substring(finalKuoHaoIndex+1)
                    .replace(StrongCommandToChatGPT.RIGHT_KUOHAO, "")
                    .trim();
        } else {
            throw new ExtractSentencesException("extractOneSentence failed");
        }
        return new Pair<>(sentence,translation);
    }

}
