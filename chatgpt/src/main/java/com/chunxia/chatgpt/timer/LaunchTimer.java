package com.chunxia.chatgpt.timer;

import android.util.Log;

public class LaunchTimer {

    private static final String TAG = "LaunchTimer";
    private static long sTime;

    public static void startRecord() {
        sTime = System.currentTimeMillis();
    }

    public static void endRecord() {
        endRecord("");
    }

    public static void endRecord(String msg) {
        long cost = System.currentTimeMillis() - sTime;
        Log.i(TAG , msg + ": " + cost);
    }

}
