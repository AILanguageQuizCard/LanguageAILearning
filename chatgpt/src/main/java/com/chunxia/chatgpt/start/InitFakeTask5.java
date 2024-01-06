package com.chunxia.chatgpt.start;


import com.chunxia.learn.start_opt.task.MainTask;
import com.chunxia.learn.start_opt.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InitFakeTask5 extends MainTask {
    @Override
    public void run() throws Exception {
        Thread.sleep(50);
    }

    @Override
    public List<Class<? extends Task>> dependsOn() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(InitFakeTask2.class);

        return arrayList;
    }
}
