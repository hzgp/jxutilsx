package com.jxkj.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static final String TAG = "ThreadUtils";

    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public  static void runOnUiThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void runOnBackgroundThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }
}
