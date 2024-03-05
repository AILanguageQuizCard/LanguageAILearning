package com.chunxia.chatgpt.start;

import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.firebase.config.RemoteConfig;
import com.chunxia.learn.start_opt.task.Task;

public class InitRemoteConfigTask extends Task {
    @Override
    public void run() throws Exception {
        RemoteConfig.RemoteConfigUpdateListener listener = new RemoteConfig.RemoteConfigUpdateListener() {
            @Override
            public void onRemoteConfigUpdate() {
                SubscriptionManager.getInstance().initSubscribe(mContext);
            }
        };
        RemoteConfig.getInstance().init(listener);

    }
}
