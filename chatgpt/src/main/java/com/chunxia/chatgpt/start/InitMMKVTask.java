package com.chunxia.chatgpt.start;

import com.chunxia.learn.start_opt.task.MainTask;
import com.chunxia.learn.start_opt.task.Task;
import com.chunxia.mmkv.KVUtils;

public class InitMMKVTask extends MainTask {

    @Override
    public void run() throws Exception {
        KVUtils.get().init(mContext);
    }
}
