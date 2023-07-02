package com.chunxia.chatgpt.model.subscribe;

import com.chunxia.mmkv.KVUtils;


// 学习一下别人是怎么写MMKV 管理类的
public class SubscriptionManager {

    private static volatile SubscriptionManager instance;

    private static String SUBSCRIPTION_KEY = "subscription_key";

    public static SubscriptionManager getInstance() {
        if (instance == null) {
            synchronized (SubscriptionManager.class) {
                if (instance == null) {
                    instance = new SubscriptionManager();
                }
            }
        }
        return instance;
    }


    private SubscriptionManager() {

    }

    public void saveSubscription(String subscription) {
        KVUtils.get().encode(SUBSCRIPTION_KEY, subscription);
    }





}