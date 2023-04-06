package com.example.chatgpt.mmkv;


import android.app.Application;

import com.tencent.mmkv.MMKV;


public class XLMMKV {

    private static volatile MMKV mmkv;

    public static MMKV getMMKV() {
        if (mmkv == null) {
            synchronized (XLMMKV.class) {
                if (mmkv == null) {
                    mmkv = MMKV.defaultMMKV();
                }
            }
        }
        return mmkv;
    }

    public static void init(Application application) {
        MMKV.initialize(application);
        mmkv = MMKV.defaultMMKV();
    }

}
