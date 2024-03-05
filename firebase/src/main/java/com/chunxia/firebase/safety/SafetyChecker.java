package com.chunxia.firebase.safety;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class SafetyChecker {

    // 把这个类变成单例
    private static SafetyChecker instance = null;
    private SafetyChecker() {

    }

    public static SafetyChecker getInstance() {
        if (instance == null) {
            instance = new SafetyChecker();
        }
        return instance;
    }


    public void check(Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

    }

}

