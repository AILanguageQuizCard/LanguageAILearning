package com.chunxia.chatgpt.model.review;

import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.google.gson.Gson;

import java.util.*;

public class ReviewCardManager {
    private final Gson gson = new Gson();
    private static final String LEARN_CARDS_KEY = "learn_cards";
    private static final String LEARN_TEST_CARDS_KEY = "learn_test_cards";
    private static final String ALL_REVIEW_DATA_KEY = "all_review_data_key";

    private AllReviewData allReviewData;


    private ReviewCardManager() {
        loadData();
    }

    private static volatile ReviewCardManager instance;

    public static ReviewCardManager getInstance() {
        if (instance == null) {
            synchronized (ReviewCardManager.class) {
                if (instance == null) {
                    instance = new ReviewCardManager();
                }
            }
        }
        return instance;
    }


    private void loadData() {
        AllReviewData temp = getAllReviewData();
        if (temp != null) {
            allReviewData = temp;
        } else {
            allReviewData = new AllReviewData();
        }
    }


    public void addOneTopicReviewSets(TopicReviewSets topicReviewSets) {
        allReviewData.addOneTopicReviewSet(topicReviewSets);
        saveAllReviewData();
    }

    public void addOneTopicReviewSets(String topic, ArrayList<SentenceCard> sentenceCards) {
        TopicReviewSets topicReviewSets = new TopicReviewSets(topic, sentenceCards);
        addOneTopicReviewSets(topicReviewSets);
    }


    public void addOneSentenceCardInTopicReviewSets(String topic, SentenceCard sentenceCard) {
        allReviewData.addOneSentenceCardInTopicReviewSets(topic, sentenceCard);
        saveAllReviewData();
    }

    public void editOneSentenceCardInTopicReviewSets(String topic, SentenceCard oldCard, SentenceCard newCard){
        allReviewData.editOneSentenceCardInTopicReviewSets(topic, oldCard, newCard);
        saveAllReviewData();
    }

    public boolean sentenceCardExistsInTopicReviewSets(String topic, SentenceCard sentenceCard) {
        return allReviewData.sentenceCardExistsInTopicReviewSets(topic, sentenceCard);
    }

    public void deleteOneSentenceCardInTopicReviewSets(String topic, SentenceCard sentenceCard) {
        allReviewData.deleteOneSentenceCardInTopicReviewSets(topic, sentenceCard);
        saveAllReviewData();
    }


    public ArrayList<SentenceCard> getSentencesCardsByTopic(String topic) {
        return allReviewData.getSentencesCardsByTopic(topic);
    }

    public TopicReviewSets getTopicReviewSetsByTopic(String topic) {
        return allReviewData.getTopicReviewSetsByTopic(topic);
    }

    public TopicReviewSets getAllTopicReviewSets() {
        return allReviewData.getAllTopicReviewSets();
    }


    public ArrayList<SentenceCard> getAllLearnCards() {
        return allReviewData.getAllLearnCards();
    }

    public boolean saveLearnTestCards(List<TopicTestCard> learnCards) {
        String learnCardsJson = gson.toJson(learnCards);
        return CXMMKV.getInstance().getMMKV().encode(LEARN_TEST_CARDS_KEY, learnCardsJson);
    }

    public List<String> getAllTopics() {
        return allReviewData.getAllTopics();
    }

    public int getSize() {
        return allReviewData.getSize();
    }


    public AllReviewData getAllReviewData() {
        return CXMMKV.getInstance().getMMKV().decodeParcelable(ALL_REVIEW_DATA_KEY, AllReviewData.class);
    }

    public boolean saveAllReviewData() {
        return CXMMKV.getInstance().getMMKV().encode(ALL_REVIEW_DATA_KEY, allReviewData);
    }


    private TopicReviewSets currentTopicReviewSets;
    public void setCurrentTopicReviewSets(TopicReviewSets current) {
        this.currentTopicReviewSets = current;
    }

    public TopicReviewSets getCurrentTopicReviewSets() {
        return currentTopicReviewSets;
    }


    private SentenceCard editedSentenceCard;
    public void setEditedSentenceCard(SentenceCard current) {
        this.editedSentenceCard = current;
    }

    public SentenceCard getEditedSentenceCard() {
        return editedSentenceCard;
    }


}
