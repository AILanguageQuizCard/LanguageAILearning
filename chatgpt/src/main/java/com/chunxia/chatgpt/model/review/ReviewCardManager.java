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
            CXMMKV.getInstance().getMMKV().encode(LEARN_CARDS_KEY, gson.toJson(new ArrayList<LearnCard>()));
        }
    }

    public static List<LearnCard> getAllLearnCards() {
        String learnCardsJson = CXMMKV.getInstance().getMMKV().decodeString(LEARN_CARDS_KEY);
        Type learnCardListType = new TypeToken<ArrayList<LearnCard>>() {
        }.getType();
        List<LearnCard> results = gson.fromJson(learnCardsJson, learnCardListType);
        if (results == null) {
            return new ArrayList<LearnCard>();
        } else {
            return results;
        }
    }

    public static boolean saveLearnCards(List<LearnCard> learnCards) {
        String learnCardsJson = gson.toJson(learnCards);
        return CXMMKV.getInstance().getMMKV().encode(LEARN_CARDS_KEY, learnCardsJson);
    }

    public static boolean saveLearnTestCards(List<TopicTestCard> learnCards) {
        String learnCardsJson = gson.toJson(learnCards);
        return CXMMKV.getInstance().getMMKV().encode(LEARN_TEST_CARDS_KEY, learnCardsJson);
    }


    public static boolean addOneLearnCard(LearnCard learnCard) {
        List<LearnCard> learnCards = getAllLearnCards();
        learnCards.add(learnCard);
        return saveLearnCards(learnCards);
    }

    public static boolean removeOneLearnCard(LearnCard learnCard) {
        List<LearnCard> learnCards = getAllLearnCards();
        if (learnCards.remove(learnCard)) {
            return saveLearnCards(learnCards);
        } else {
            return false;
        }
    }
}
