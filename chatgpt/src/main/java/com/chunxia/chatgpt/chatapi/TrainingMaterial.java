package com.chunxia.chatgpt.chatapi;

import android.util.Log;

import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.chunxia.chatgpt.mmkv.MMKVConstant;
import com.chunxia.chatgpt.model.review.SentenceCard;
import com.chunxia.chatgpt.model.review.LearnRecord;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.model.review.TopicTestCard;

import java.util.ArrayList;
import java.util.concurrent.Callable;

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
    private int sentenceN = 5;
    private int questionN = 3;
    private final int maxTokenN = 1024;

    private ArrayList<SentenceCard> sentenceCards;
    private ArrayList<TopicTestCard> topicTestCards;



    public TrainingMaterial(int sentenceN, int questionN) {
        this.sentenceN = sentenceN;
        this.questionN = questionN;
        this.motherLanguage = CXMMKV.getInstance().getMMKV().getString(MMKVConstant.SETTING_RECORDING_VOICE_LANGUAGE_KEY,
                MMKVConstant.SETTING_RECORDING_VOICE_LANGUAGE_DEFAULT_VALUE);

        this.learningLanguage = CXMMKV.getInstance().getMMKV().getString(MMKVConstant.SETTING_VOICE_LANGUAGE_KEY,
                MMKVConstant.SETTING_VOICE_LANGUAGE_DEFAULT_VALUE);

    }

    public TrainingMaterial(){
        this.motherLanguage = CXMMKV.getInstance().getMMKV().getString(MMKVConstant.SETTING_RECORDING_VOICE_LANGUAGE_KEY,
                MMKVConstant.SETTING_RECORDING_VOICE_LANGUAGE_DEFAULT_VALUE);

        this.learningLanguage = CXMMKV.getInstance().getMMKV().getString(MMKVConstant.SETTING_VOICE_LANGUAGE_KEY,
                MMKVConstant.SETTING_VOICE_LANGUAGE_DEFAULT_VALUE);

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
        Observable<String> stringObservable = getTrainingSentencesObservable(topic).subscribeOn(Schedulers.io());
        Observable<String> stringObservable1 = getTrainingQuestionAndAnswerObservable(topic).subscribeOn(Schedulers.io());
        Observable.zip(stringObservable, stringObservable1, Result::new)
                .observeOn(AndroidSchedulers.mainThread())  // 在主线程处理请求结果
                .subscribe(
                        new DisposableObserver<Result>() {
                            @Override
                            public void onNext(@NonNull final Result result) {
                                sentenceCards = ChatGptResponseTools.extractTopicTrainingSentences(result.s1);
                                topicTestCards = ChatGptResponseTools.extractTopicTrainingQuestions(result.s2);
                                callback.onReceiveData(sentenceCards, topicTestCards);
                            }

                            @Override
                            public void onError(@NonNull final Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
    }

    public void prepareSentencePatternExamplesData(String sentencePattern, ReceiveSentencePatternExamplesCallback callback) {
        Observable<String> stringObservable = getSentencesPatternExamplesObservable(sentencePattern).subscribeOn(Schedulers.io());
        stringObservable
                .observeOn(AndroidSchedulers.mainThread())  // 在主线程处理请求结果
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull final String result) {
                        callback.onReceiveData(result);
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getOpinionObservable(String opinion, ReceiveSentencePatternExamplesCallback callback) {
        Observable<String> stringObservable = getOpinionObservable(opinion).subscribeOn(Schedulers.io());
        stringObservable
                .observeOn(AndroidSchedulers.mainThread())  // 在主线程处理请求结果
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull final String result) {
                        callback.onReceiveData(result);
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void prepareSentenceData(String topic, ReceiveTrainMaterialCallback callback) {
        Observable<String> stringObservable = getTrainingSentencesObservable(topic).subscribeOn(Schedulers.io());
        stringObservable
                .observeOn(AndroidSchedulers.mainThread())  // 在主线程处理请求结果
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull final String result) {
                        sentenceCards = ChatGptResponseTools.extractTopicTrainingSentences(result);
                        callback.onReceiveData(sentenceCards, null);
                    }

                    @Override
                    public void onError(@NonNull final Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public interface ReceiveTrainMaterialCallback {
        void onReceiveData(ArrayList<SentenceCard> sentenceCards, ArrayList<TopicTestCard> topicTestCards);
    }

    public interface ReceiveSentencePatternExamplesCallback {
        void onReceiveData(String reply);
    }


    public Observable<String> getSentencesPatternExamplesObservable(String sentencePattern) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.i(TAG, "getSentencesPatternExamplesObservable");
                MultiRoundChatAgent agent = new MultiRoundChatAgent();
                agent.setMaxTokenN(maxTokenN);
                // todo 获取母语设置
                String prompt = StrongCommandToChatGPT.getSentencePatternExamplesPrompt(sentencePattern, learningLanguage, sentenceN);
                Log.i(TAG, prompt);
                long start = System.currentTimeMillis();

                String result = agent.sendMessage(prompt);
                long end = System.currentTimeMillis();
                Log.i(TAG, "get training sentences time: " + (end - start));
                return result;
            }
        });
    }

    public Observable<String> getOpinionObservable(String opinion) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.i(TAG, "getOpinionObservable");
                MultiRoundChatAgent agent = new MultiRoundChatAgent();
                agent.setMaxTokenN(maxTokenN);
                // todo 获取母语设置
                String prompt = StrongCommandToChatGPT.getOpinionPrompt(opinion, learningLanguage);
                Log.i(TAG, prompt);
                long start = System.currentTimeMillis();

                String result = agent.sendMessage(prompt);
                long end = System.currentTimeMillis();
                Log.i(TAG, "get training sentences time: " + (end - start));
                return result;
            }
        });
    }


    public Observable<String> getTrainingSentencesObservable(String topic) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.i(TAG, "getTrainingSentencesObservable");
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


    public Observable<String> getTrainingQuestionAndAnswerObservable(String topic) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.i(TAG, "getTrainingQuestionAndAnswerObservable");
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
        ReviewCardManager.getInstance().saveLearnTestCards(results);
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
