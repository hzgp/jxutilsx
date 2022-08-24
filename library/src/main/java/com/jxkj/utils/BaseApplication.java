package com.jxkj.utils;

import android.app.Application;
import android.os.Bundle;


public class BaseApplication extends Application {
    protected static BaseApplication context;

    public static BaseApplication getContext() {
        return context;
    }

    public void startChatActivity(Bundle bundle) {
    }

    public void startMainActivity() {
    }

}
