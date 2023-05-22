package com.chunxia.chatgpt.chatapi;

import com.chunxia.chatgpt.model.review.LearnCard;

import java.util.ArrayList;

public class ChatGptResponseTools {

    public static ArrayList<LearnCard> extractTopicTrainingSentences(String input) {
        ArrayList<LearnCard> learnCards = new ArrayList<>();
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
        for (String word : words) {
            if (word.isEmpty()) continue;
            String deleteS = (sentenceCount + ".");
            String res = word.replace(deleteS, "").trim();
            learnCards.add(extractOneSentence(res));
            sentenceCount++;
        }

        return learnCards;
    }

    private static LearnCard extractOneSentence(String res) {
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
        }
        return new LearnCard(sentence, translation);
    }

}
