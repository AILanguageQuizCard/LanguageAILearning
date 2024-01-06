package com.chunxia.chatgpt.start;

import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.learn.start_opt.task.Task;

public class InitFirebaseTask extends Task {
    @Override
    public void run() throws Exception {
        FirebaseInstanceIDManager.getInstance().checkUserStatusWhenLauncher(mContext);
    }
}
