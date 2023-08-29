package com.chunxia.firebase.model;

import androidx.annotation.NonNull;

import java.util.Map;

public class User {


    public static final String remainingTrailKey = "remaining_trail";
    Long remainingTrail;

    String androidId;

    public static final String isSubscribedKey = "is_subscribed";
    boolean isSubscribed;

    public User() {
        super();
    }

    public User(Long remainingTrail, String androidId, boolean isSubscribed) {
        this.remainingTrail = remainingTrail;
        this.androidId = androidId;
        this.isSubscribed = isSubscribed;
    }

    public Long getRemainingTrail() {
        return remainingTrail;
    }

    public void setRemainingTrail(Long remainingTrail) {
        this.remainingTrail = remainingTrail;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void decreaseRemainingTrail() {
        if (this.remainingTrail >= 1) {
            this.remainingTrail = this.remainingTrail - 1 ;
        } else {
            this.remainingTrail = 0L;
        }
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public static User firebaseMap2User(Map<String, Object> map) {
        User user = new User();
        // 如果map中有remainingTrailKey，那么更新用户的remainingTrail
        if (map.containsKey(remainingTrailKey)) {
            user.setRemainingTrail((Long) map.get(remainingTrailKey));
        }
        if (map.containsKey(isSubscribedKey)) {
            user.setSubscribed((Boolean) map.getOrDefault(isSubscribedKey, false));
        }

        return user;
    }

    public static User firebaseMap2User(@NonNull User oldUser, Map<String, Object> map) {
        // 使用旧用户作为基础创建一个新用户
        User user = new User(oldUser.getRemainingTrail(), oldUser.getAndroidId(), oldUser.isSubscribed());

        // 如果map中有remainingTrailKey，那么更新用户的remainingTrail
        if (map.containsKey(remainingTrailKey)) {
            user.setRemainingTrail((Long) map.get(remainingTrailKey));
        }

        return user;
    }


}
