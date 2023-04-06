package com.example.chatgpt.common;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.example.chatgpt.mmkv.XLMMKV;


public class XLApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        XLMMKV.init(this);
    }
}
