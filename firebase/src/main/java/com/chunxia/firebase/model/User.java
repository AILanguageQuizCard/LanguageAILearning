package com.chunxia.firebase.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chunxia.firebase.R;

import java.util.Map;

public class User {
    public static final String remainingTrailKey = "remaining_trail";
    private Long remainingTrail;

    private String androidId;

    public static final String startTrailTimeKey = "start_trail_time";
    private Long startTrailTime;

    public static final String isSubscribedKey = "is_subscribed";
    private boolean isSubscribed;

    public User() {
        super();
    }


    public User(Long remainingTrail, String androidId, Long startTrailTime, boolean isSubscribed) {
        this.remainingTrail = remainingTrail;
        this.androidId = androidId;
        this.startTrailTime = startTrailTime;
        this.isSubscribed = isSubscribed;
    }

    public boolean trailIsOver() throws UserUnInitException {
        if (startTrailTime == null) {
            throw new UserUnInitException("User may be not inited, startTrailTime is null");
        } else {
            long now = System.currentTimeMillis();
            long threeDays = 3 * 24 * 60 * 60 * 1000;
            return now - threeDays > startTrailTime;
        }
    }

    public String getRemainingTrailTimeString(Context context) throws UserUnInitException {
        if (startTrailTime == null) {
            throw new UserUnInitException("User may be not inited, startTrailTime is null");
        } else {
            long threeDays = 3 * 24 * 60 * 60 * 1000;
            long diff = startTrailTime + threeDays - System.currentTimeMillis() ; //获取两个时间点之间的差值，单位：毫秒

            // 将差值转换为天、小时、分钟
            long days = diff / (24 * 60 * 60 * 1000);
            diff %= (24 * 60 * 60 * 1000);
            long hours = diff / (60 * 60 * 1000);
            diff %= (60 * 60 * 1000);
            long minutes = diff / (60 * 1000);

            // todo 换成其他语言
            return days + context.getString(R.string.day_string)+" "
                    + hours + context.getString(R.string.hour_string) + " "
                    + minutes + context.getString(R.string.minute_string);
        }
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
            this.remainingTrail = this.remainingTrail - 1;
        } else {
            this.remainingTrail = 0L;
        }
    }

    public Long getStartTrailTime() {
        return startTrailTime;
    }

    public void setStartTrailTime(Long startTrailTime) {
        this.startTrailTime = startTrailTime;
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

    private static void update(Map<String, Object> map, User user) {
        if (map.containsKey(remainingTrailKey)) {
            user.setRemainingTrail((Long) map.get(remainingTrailKey));
        }
        if (map.containsKey(isSubscribedKey)) {
            user.setSubscribed((Boolean) map.getOrDefault(isSubscribedKey, false));
        }
        if (map.containsKey(startTrailTimeKey)) {
            user.setStartTrailTime((Long) map.getOrDefault(startTrailTimeKey, 0L));
        }
    }


    public static User firebaseMap2User(Map<String, Object> map) {
        User user = new User();
        update(map, user);
        return user;
    }

    public static User firebaseMap2User(@NonNull User oldUser, Map<String, Object> map) {
        // 使用旧用户作为基础创建一个新用户
        User user = new User(oldUser.getRemainingTrail(), oldUser.getAndroidId(), oldUser.getStartTrailTime(), oldUser.isSubscribed());
        update(map, user);
        return user;
    }


}
