package com.binancetracker.thread;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RestExecuter {
    private static final String TAG = RestExecuter.class.getSimpleName();

    private final static RestExecuter restExecutor = new RestExecuter();

    private RestThreadManager restThreadManager;

    private RestExecuter()
    {
        restThreadManager = new RestThreadManager();
    }

    public static void addTask(Runnable runnable)
    {
        restExecutor.restThreadManager.requestExecutor.execute(runnable);
    }

    private class RestThreadManager
    {
        private static final long KEEP_ALIVE_TIME = 50;
        private final BlockingQueue<Runnable> requestqueue;
        private final ThreadPoolExecutor requestExecutor;

        public RestThreadManager()
        {
            requestqueue = new ArrayBlockingQueue<>(10);

            requestExecutor = new ThreadPoolExecutor(
                    1,       // Initial pool size
                    4,       // Max pool size
                    KEEP_ALIVE_TIME,
                    TimeUnit.MILLISECONDS,
                    requestqueue);
            //handel case that queue is full, and wait till its free
            requestExecutor.setRejectedExecutionHandler((r, executor) -> {
                Log.d(TAG, "imageSave Queue full");
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
