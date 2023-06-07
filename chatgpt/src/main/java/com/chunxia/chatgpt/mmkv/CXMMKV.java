package com.chunxia.chatgpt.mmkv;

import android.app.Application;

import com.tencent.mmkv.MMKV;


public class CXMMKV {

    private static volatile CXMMKV instance;

    private final MMKV mmkv;

    public CXMMKV(MMKV mmkv) {
        this.mmkv = mmkv;
    }

    public static CXMMKV getInstance() {
        if (instance == null) {
            synchronized (CXMMKV.class) {
                if (instance == null) {
                    instance = new CXMMKV(MMKV.defaultMMKV());
                }
            }
        }
        return instance;
    }

    public MMKV getMMKV() {
        return mmkv;
    }

    public static void init(Application application) {
        MMKV.initialize(application);
    }


}
