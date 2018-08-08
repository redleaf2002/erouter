package com.leaf.erouter.api.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by suhong01 on 2018/8/3.
 */

public class ExecutorManager {
    private static volatile ExecutorManager mExecutorManager;
    private static Object object = new Object();
    private ExecutorService executorService;

    private ExecutorManager() {
    }

    public static ExecutorManager getInstance() {
        if (mExecutorManager == null) {
            synchronized(object) {
                if (mExecutorManager == null) {
                    mExecutorManager = new ExecutorManager();
                }
            }
        }
        return mExecutorManager;
    }

    public void execute(Runnable runnable) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(3);
        }
        executorService.execute(runnable);
    }

}
