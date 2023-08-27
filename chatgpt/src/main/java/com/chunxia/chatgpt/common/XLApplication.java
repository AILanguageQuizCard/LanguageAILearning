package com.chunxia.chatgpt.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.firebase.config.RemoteConfig;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
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

        RemoteConfig.RemoteConfigUpdateListener listener = new RemoteConfig.RemoteConfigUpdateListener() {
            @Override
            public void onRemoteConfigUpdate() {
                SubscriptionManager.getInstance().initSubscribe(XLApplication.this);
            }
        };
        RemoteConfig.getInstance().init(listener);

        FirebaseInstanceIDManager.getInstance().checkUserStatusWhenLauncher(XLApplication.this);
    }

}
