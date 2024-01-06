package com.chunxia.chatgpt.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.chunxia.chatgpt.start.InitFakeTask;
import com.chunxia.chatgpt.start.InitFakeTask2;
import com.chunxia.chatgpt.start.InitFakeTask3;
import com.chunxia.chatgpt.start.InitFakeTask4;
import com.chunxia.chatgpt.start.InitFakeTask5;
import com.chunxia.chatgpt.start.InitFirebaseTask;
import com.chunxia.chatgpt.start.InitMMKVTask;
import com.chunxia.chatgpt.start.InitRemoteConfigTask;
import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.chatgpt.timer.LaunchTimer;
import com.chunxia.firebase.config.RemoteConfig;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.learn.start_opt.TaskDispatcher;
import com.chunxia.mmkv.KVUtils;


public class XLApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LaunchTimer.startRecord();
        MultiDex.install(this);
    }


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LaunchTimer.startRecord();
        TaskDispatcher.init(XLApplication.this);

        TaskDispatcher dispatcher = TaskDispatcher.createInstance();

        dispatcher.addTask(new InitMMKVTask())
                .addTask(new InitRemoteConfigTask())
                .addTask(new InitFirebaseTask())
                .addTask(new InitFakeTask())
                .addTask(new InitFakeTask2())
                .addTask(new InitFakeTask3())
                .addTask(new InitFakeTask4())
                .addTask(new InitFakeTask5())
                .start();

        dispatcher.await();

        LaunchTimer.endRecord("TaskDispatcher");

//        如果不使用TaskDispatcher，可以使用下面的代码
//        KVUtils.get().init(this);
//        RemoteConfig.RemoteConfigUpdateListener listener = new RemoteConfig.RemoteConfigUpdateListener() {
//            @Override
//            public void onRemoteConfigUpdate() {
//                SubscriptionManager.getInstance().initSubscribe(XLApplication.this);
//            }
//        };
//        RemoteConfig.getInstance().init(listener);
//
//        FirebaseInstanceIDManager.getInstance().checkUserStatusWhenLauncher(this);
//        try {
//            new InitFakeTask().run();
//            new InitFakeTask2().run();
//            new InitFakeTask3().run();
//            new InitFakeTask4().run();
//            new InitFakeTask5().run();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        LaunchTimer.endRecord("XLApplication");

    }

}
