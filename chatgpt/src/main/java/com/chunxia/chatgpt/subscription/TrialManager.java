package com.chunxia.chatgpt.subscription;

/**
 * Created by lyk on 2023/7/10
 * 管理免费试用
 */
public class TrialManager {

        private static final String TAG = "TrialManager";
        private volatile static TrialManager instance;

        private TrialManager() {}

        public static TrialManager getInstance() {
            if (instance == null) {
                synchronized (TrialManager.class) {
                    if (instance == null) {
                        instance = new TrialManager();
                    }
                }
            }
            return instance;
        }


        //  todo 从服务器获取最新的试用信息
        public void initTrial() {
            // 1. 获取唯一当前手机的唯一id
            // 2. 从服务器获取试用信息
            // 3. 保存到本地
        }



}
