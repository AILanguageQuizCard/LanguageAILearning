package com.chunxia.chatgpt.chatapi;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.Observer;

import com.chunxia.chatgpt.model.review.LearnCard;
import com.chunxia.chatgpt.model.review.LearnRecord;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.model.review.TopicTestCard;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TrainingMaterial {

    //    private final MultiRoundChatAgent agent;
    private final String TAG = "TrainingMaterial";
    private String learningLanguage = "English";
    private String motherLanguage = "Chinese";
    private int sentenceN = 10;
    private int questionN = 3;
    private final int maxTokenN = 1024;

    private ArrayList<LearnCard> learnCards;
    private ArrayList<TopicTestCard> topicTestCards;


    public TrainingMaterial() {

    }

    public static class Result {
        String s1;
        String s2;

        public Result(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }


    public void prepareData(String topic, ReceiveTrainMaterialCallback callback) {
        Observable.zip(
                        getTrainingSentencesObservable(topic),
                        getTrainingQuestionAndAnswerObservable(topic),
                        Result::new
                )
                .subscribeOn(Schedulers.io())  // 在IO线程中执行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 在主线程处理请求结果
                .subscribe(
                        new DisposableObserver<Result>() {
                            @Override
                            public void onNext(@NonNull final Result result) {
                                learnCards = ChatGptResponseTools.extractTopicTrainingSentences(result.s1);
                                saveLearnCards(learnCards);
                                topicTestCards = ChatGptResponseTools.extractTopicTrainingQuestions(result.s2);
                                saveLearnTestCards(topicTestCards);
                                callback.onReceiveData(learnCards, topicTestCards);
                            }

                            @Override
                            public void onError(@NonNull final Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }
                );

    }

    public interface ReceiveTrainMaterialCallback {
        void onReceiveData(ArrayList<LearnCard> learnCards, ArrayList<TopicTestCard> topicTestCards);
    }


    public Observable<String> getTrainingSentencesObservable(String topic) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                MultiRoundChatAgent agent = new MultiRoundChatAgent();
                agent.setMaxTokenN(maxTokenN);
                // todo 获取母语设置
                String prompt = StrongCommandToChatGPT.getCompleteTopicTrainingPrompt(topic, learningLanguage, motherLanguage, sentenceN);
                Log.i(TAG, prompt);
                long start = System.currentTimeMillis();

                String result = agent.sendMessage(prompt);
                long end = System.currentTimeMillis();
                Log.i(TAG, "get training sentences time: " + (end - start));
                return result;
            }
        });
    }


    public void saveLearnCards(ArrayList<LearnCard> results) {
        for (LearnCard learnCard : results) {
            learnCard.setLearnRecord(new LearnRecord());
        }
        ReviewCardManager.saveLearnCards(results);
    }


    public Observable<String> getTrainingQuestionAndAnswerObservable(String topic) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                MultiRoundChatAgent agent = new MultiRoundChatAgent();
                agent.setMaxTokenN(maxTokenN);
                // todo 获取母语设置
                String prompt = StrongCommandToChatGPT.getCompleteTopicTrainingQuestionPrompt(topic, learningLanguage, motherLanguage, questionN);
                Log.i(TAG, prompt);

                long start = System.currentTimeMillis();
                String result = agent.sendMessage(prompt);
                long end = System.currentTimeMillis();
                Log.i(TAG, "get training question time: " + (end - start));
                return result;
            }
        });
    }


    public void saveLearnTestCards(ArrayList<TopicTestCard> results) {
        for (TopicTestCard learnCard : results) {
            learnCard.setLearnRecord(new LearnRecord());
        }
        ReviewCardManager.saveLearnTestCards(results);
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