package com.chunxia.chatgpt.start;

import com.chunxia.learn.start_opt.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InitFakeTask2 extends Task {
    @Override
    public void run() throws Exception {
        Thread.sleep(20);
    }

    @Override
    public List<Class<? extends Task>> dependsOn() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(InitFakeTask.class);
        return arrayList;
    }
}
