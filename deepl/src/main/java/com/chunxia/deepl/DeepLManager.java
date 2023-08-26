package com.chunxia.deepl;

import com.chunxia.firebase.RealtimeDatabase;

public class DeepLManager {

    public static String getDeepLApiKey() {
        return apiKey;
    }

    private volatile static String apiKey = "";

    private volatile static boolean isInit = false;

    public static boolean isInit() {
        return isInit;
    }

    public static void initApiKey() {
        RealtimeDatabase.getInstance().getDeepLApiKeyOnce(new RealtimeDatabase.onRealtimeDatabaseListener() {
            @Override
            public void onDataChange(String apiKey) {
                setApiKey(apiKey);
                isInit = true;
            }
        });
    }


    private static void setApiKey(String key) {
        apiKey = key;
    }


}
