package com.chunxia.chatgpt.chatapi;

import android.util.Log;

import com.chunxia.chatgpt.model.review.LearnCard;
import com.chunxia.chatgpt.model.review.LearnRecord;
import com.chunxia.chatgpt.model.review.ReviewCardManager;

import java.util.ArrayList;

public class TrainingMaterial {

    private final String TAG = "TrainingMaterial";
    private String learningLanguage = "English";
    private String motherLanguage = "Chinese";
    private int sentenceN = 10;

    public void getTrainingSentences(String topic, onReceiveTrainingSentences onReceiveTrainingSentences) {
        MultiRoundChatAgent agent = new MultiRoundChatAgent();
        agent.setMaxTokenN(1024);

        // todo 获取母语设置
        String prompt = StrongCommandToChatGPT.getCompleteTopicTrainingPrompt(topic, learningLanguage, motherLanguage, sentenceN);
        Log.i(TAG, prompt);
        MultiRoundChatAgent.ReceiveOpenAiReply receiveOpenAiReply = new MultiRoundChatAgent.ReceiveOpenAiReply() {
            @Override
            public void onSuccess(String reply) {
                Log.i(TAG, "reply \n " + reply);
                ArrayList<LearnCard> results = ChatGptResponseTools.extractTopicTrainingSentences(reply);
                Log.i(TAG, "10 sentences: \n" + results);
                onReceiveTrainingSentences.onSuccess(sentenceN, results);
                saveLearnCards(results);
            }
        };
        agent.sendMessageInThread(prompt, receiveOpenAiReply);
    }


    public void saveLearnCards(ArrayList<LearnCard> results) {
        for (LearnCard learnCard:results) {
            learnCard.setLearnRecord(new LearnRecord());
        }
        ReviewCardManager.saveLearnCards(results);
    }

    public interface onReceiveTrainingSentences {
        void onSuccess(int n, ArrayList<LearnCard> learnCards);
    }


    public void getTrainingQuestionAndAnswer() {

    }


    public void getTrainingSentencesTranslation() {

    }


    public void getTrainingArticle() {

    }


    public String getLearningLanguage() {
        return learningLanguage;
    }

    public void setLearningLanguage(String learningLanguage) {
        this.learningLanguage = learningLanguage;
    }

    public String getMotherLanguage() {
        return motherLanguage;
    }

    public void setMotherLanguage(String motherLanguage) {
        this.motherLanguage = motherLanguage;
    }
}
