package com.jxkj.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Author:zhujb
 * Date:2020/9/3
 */
public class DownLoadProgressManager {
    private Map<Object, DownLoadProgressListener> listeners;

    private DownLoadProgressManager() {
        listeners = new HashMap<>();
    }

    public void registerListener(Object key, DownLoadProgressListener listener) {
        if (!listeners.containsKey(key)) {
            listeners.put(key, listener);
        }
    }
    public boolean hasListener(Object key){
        return listeners.containsKey(key);
    }
    public DownLoadProgressListener getListener(Object key){
        return listeners.get(key);
    }
    public void unRegisterListener(Object key) {
        listeners.remove(key);
    }

    public int getListenerCount(){
        return listeners.size();
    }
    public static DownLoadProgressManager getInstance() {
        return ManagerHolder.manager;
    }

    private static class ManagerHolder {
        private static DownLoadProgressManager manager = new DownLoadProgressManager();
    }
}
