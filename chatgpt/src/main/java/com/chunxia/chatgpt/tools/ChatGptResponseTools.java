package com.chunxia.chatgpt.tools;

import java.util.ArrayList;

public class ChatGptResponseTools {

    public static ArrayList<String> extractTopicTrainingSentences(String input, int n) {
        ArrayList<String> sentences = new ArrayList<>();
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
        }
        String newInput = input.substring(finalStartIndex);

        String[] words = newInput.split("\n");
        int sentenceCount = 1;
        boolean result = true;
        for (String word : words) {
            if (word.isEmpty()) continue;
            int startIndex = word.indexOf(String.valueOf(sentenceCount));
            if (startIndex == -1) result = false;
            String deleteS = (sentenceCount + ".");
            String res = word.replace(deleteS, "").trim();
            sentences.add(res);
            sentenceCount++;
        }

        if (sentences.size() != n && (sentences.size() == 1 || sentences.size() == 0)) {
            sentences.clear();
            for (String word: words ) {
                if (!word.isEmpty()) {
                    sentences.add(word);
                }
            }
        }

        return sentences;
    }


}