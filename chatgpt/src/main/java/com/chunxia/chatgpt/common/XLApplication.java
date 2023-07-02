package com.chunxia.chatgpt.common;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chunxia.mmkv.KVUtils;


public class XLApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        KVUtils.get().init(getApplicationContext());
    }
}
