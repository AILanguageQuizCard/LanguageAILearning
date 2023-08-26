package com.chunxia.chatgpt.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chunxia.chatgpt.chatapi.OpenAIServiceManager;
import com.chunxia.deepl.DeepLManager;
import com.chunxia.mmkv.KVUtils;

import darren.googlecloudtts.GoogleCloudText2VoiceManager;


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

        OpenAIServiceManager.initApiKey();
        GoogleCloudText2VoiceManager.initApiKey();
        DeepLManager.initApiKey();
    }

}
