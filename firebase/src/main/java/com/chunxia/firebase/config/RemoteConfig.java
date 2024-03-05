package com.chunxia.firebase.config;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RemoteConfig {

    private static final String TAG = "RemoteConfig";
    private static volatile RemoteConfig instance;

    public static RemoteConfig getInstance() {
        if (instance == null) {
            synchronized (RemoteConfig.class) {
                if (instance == null) {
                    instance = new RemoteConfig();
                }
            }
        }
        return instance;
    }

    private final FirebaseRemoteConfig mFirebaseRemoteConfig;

    private RemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    boolean isInitialized = false;


    public static interface RemoteConfigUpdateListener {
        void onRemoteConfigUpdate();
    }


    public void init(RemoteConfigUpdateListener listener) {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            listener.onRemoteConfigUpdate();
                            isInitialized = true;
                        } else {
                            Log.d(TAG, "Config params updated failed");
                            isInitialized = false;
                        }
                    }
                });
    }

    public String getOpenAIApiKey() {
        if (isInitialized) {
            return mFirebaseRemoteConfig.getString("open_ai_api_key");
        } else {
            return null;
        }
    }

    public String getLatestAppVersion() {
        if (isInitialized) {
            return mFirebaseRemoteConfig.getString("latest_app_version");
        } else {
            return null;
        }
    }

    public String getSubscriptionKey() {
        return mFirebaseRemoteConfig.getString("subscription_key");
    }


    public String getGoogleCloudApiKey() {
        // 这个api key只是google text to speech的api key
        if (isInitialized) {
            return mFirebaseRemoteConfig.getString("google_cloud_api_key");
        } else {
            return null;
        }
    }

    public String getDeepLApiKey() {
        if (isInitialized) {
            return mFirebaseRemoteConfig.getString("deepl_api_key");
        } else {
            return null;
        }
    }



}
