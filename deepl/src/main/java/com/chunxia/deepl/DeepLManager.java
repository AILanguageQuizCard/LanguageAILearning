package com.chunxia.deepl;

import com.chunxia.firebase.config.RemoteConfig;

public class DeepLManager {

    public static String getDeepLApiKey() {
        return RemoteConfig.getInstance().getDeepLApiKey();
    }

}
