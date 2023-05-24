package com.chunxia.chatgpt.model.review;

import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class ReviewCardManager {
    private static final Gson gson = new Gson();
    private static final String LEARN_CARDS_KEY = "learn_cards";
    private static final String LEARN_TEST_CARDS_KEY = "learn_test_cards";

    public static void initReviewCardsList() {
        if (CXMMKV.getInstance().getMMKV().decodeString(LEARN_CARDS_KEY) == null) {
            CXMMKV.getInstance().getMMKV().encode(LEARN_CARDS_KEY, gson.toJson(new ArrayList<SentenceCard>()));
        }
    }

    public static List<SentenceCard> getAllLearnCards() {
        String learnCardsJson = CXMMKV.getInstance().getMMKV().decodeString(LEARN_CARDS_KEY);
        Type learnCardListType = new TypeToken<ArrayList<SentenceCard>>() {
        }.getType();
        List<SentenceCard> results = gson.fromJson(learnCardsJson, learnCardListType);
        if (results == null) {
            return new ArrayList<SentenceCard>();
        } else {
            return results;
        }
    }

    public static boolean saveLearnCards(List<SentenceCard> sentenceCards) {
        String learnCardsJson = gson.toJson(sentenceCards);
        return CXMMKV.getInstance().getMMKV().encode(LEARN_CARDS_KEY, learnCardsJson);
    }

    public static boolean saveLearnTestCards(List<TopicTestCard> learnCards) {
        String learnCardsJson = gson.toJson(learnCards);
        return CXMMKV.getInstance().getMMKV().encode(LEARN_TEST_CARDS_KEY, learnCardsJson);
    }


    public static boolean addOneLearnCard(SentenceCard sentenceCard) {
        List<SentenceCard> sentenceCards = getAllLearnCards();
        sentenceCards.add(sentenceCard);
        return saveLearnCards(sentenceCards);
    }

    public static boolean removeOneLearnCard(SentenceCard sentenceCard) {
        List<SentenceCard> sentenceCards = getAllLearnCards();
        if (sentenceCards.remove(sentenceCard)) {
            return saveLearnCards(sentenceCards);
        } else {
            return false;
        }
    }
}
