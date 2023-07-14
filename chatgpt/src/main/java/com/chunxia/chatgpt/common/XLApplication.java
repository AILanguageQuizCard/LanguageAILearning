package com.chunxia.chatgpt.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chunxia.mmkv.KVUtils;


public class XLApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KVUtils.get().init(getApplicationContext());
        context = getApplicationContext();
    }

}
