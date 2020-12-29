package com.jxkj.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by admin on 2018/7/5.
 */

public class AppManager {

    private Stack<Activity> activityStack;
    private static AppManager instance;
    private Link link;

    private AppManager() {

    }

    /**
     * 单一实例
     */
    public synchronized static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
//        if (activity instanceof IHead) {
//            Bundle linkExtra = ((IHead) activity).getLinkExtra();
//            if (link == null) {
//                link = new Link();
//            }
//            link.offerHead(activity);
//            link.linkExtras.putAll(linkExtra);
//        } else {
//            if (link != null && !link.isEmpty()) {
//                link.offerItem(activity);
//            }
//        }
        activityStack.add(activity);
    }

    public boolean appendLinkExtra(Activity activity, Bundle bundle) {
//        if (isLinkItem(activity)) {
//            link.appendAllExtras(bundle);
//            return true;
//        }
        return false;
    }

    public boolean appendLinkExtra(Activity activity, String key, Object value) {
        if (isLinkItem(activity)) {
            link.appendExtra(key, value);
            return true;
        }
        return false;
    }

    public Bundle getLinkExtra(Activity activity) {
        return activity == null || link == null || !link.has(activity) ? null : link.linkExtras;
    }

    private boolean isLinkItem(Activity activity) {
        return activity != null && link != null && link.has(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            remove(activity);
            activity.finish();
        }
    }

    public void remove(Activity activity) {
//        if (link != null) {
//            link.remove(activity);
//            if (link.isEmpty()) {
//                link = null;
//            }
//        }
        activityStack.remove(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Activity activity ;
        for(int i=activityStack.size()-1;i>=0;i--){
            activity =activityStack.get(i);
            if (activity.getClass().getName().equals(cls.getName())) {
                activityStack.remove(i);
                activity.finish();
            }
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?>... cls ) {
        Activity activity ;
        for(int i=activityStack.size()-1;i>=0;i--){
            activity =activityStack.get(i);
            for (Class<?> mClass:cls){
                if (activity.getClass().getName().equals(mClass.getName())) {
                    activityStack.remove(i);
                    activity.finish();
                }
            }
        }
    }
    public void tryFinishSurfaceLink(Activity activity) {

    }

    public void tryFinishWholeLink(Activity activity) {

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 获得上一个Activity
     */
    public Activity getPreviousTask() {
        int taskCount = activityStack.size();
        return taskCount < 2 ? null : activityStack.get(activityStack.size() - 2);
    }

    public Activity getTaskTop() {
        return activityStack.get(activityStack.size() - 1);
    }


    public static interface IHead {
        Bundle getLinkExtra();
    }

    private class Link {
        private Map<Integer, LinkChild> linkChildMap;
        private LinkChild currentLinkChild;
        private List<LinkChild> linkChildList;

        private class LinkChild {
            private ArrayList<Activity> linkItems;
            private int oweLinkHead = -1;
            private int linkHead;

            private LinkChild() {
                linkItems = new ArrayList<>();
            }

            private boolean has(Activity activity) {
                return linkItems.contains(activity);
            }

            private void updateHead(LinkChild highLevel) {
                linkItems.remove(0);
                highLevel.linkItems.addAll(linkItems);
            }

            private boolean removeItem(Activity activity) {
                return linkItems.remove(activity);
            }
        }

        private Link() {
            linkChildMap = new LinkedHashMap<>();
            linkChildList = new ArrayList<>();
            linkExtras = new Bundle();
        }

        private void offerHead(Activity activity) {
            int oweLinkHead = currentLinkChild == null ? -1 : currentLinkChild.linkHead;
            currentLinkChild = new LinkChild();
            currentLinkChild.linkHead = activity.hashCode();
            currentLinkChild.oweLinkHead = oweLinkHead;
            linkChildMap.put(currentLinkChild.linkHead, currentLinkChild);
            linkChildList.add(currentLinkChild);
        }

        private void offerItem(Activity activity) {
            currentLinkChild.linkItems.add(activity);
        }

        private Bundle linkExtras;

        private int remove(Activity activity) {
            return activity instanceof IHead ? removeHead(activity) : removeItem(activity);
        }

        private boolean isEmpty() {
            return linkChildMap.isEmpty();
        }

        private int removeHead(Activity activity) {
            LinkChild item;
            for (int i = linkChildList.size() - 1; i >= 0; i--) {
                item = linkChildList.get(i);
                if (item.has(activity)) {

                    LinkChild highLevel = linkChildMap.get(item.oweLinkHead);
                    if (highLevel != null) {
                        item.updateHead(highLevel);
                    }
                    linkChildMap.remove(item.linkHead);
                    linkChildList.remove(item);
                    return i;
                }
            }
            return -1;
        }

        private int removeItem(Activity activity) {
            LinkChild item;
            for (int i = linkChildList.size() - 1; i >= 0; i--) {
                item = linkChildList.get(i);
                if (item.removeItem(activity)) {
                    return i;
                }
            }
            return -1;
        }

        boolean has(Activity activity) {
            LinkChild item;
            for (int i = linkChildList.size() - 1; i >= 0; i--) {
                item = linkChildList.get(i);
                if (item.has(activity)) {
                    return true;
                }
            }
            return false;
        }

        private void appendAllExtras(Bundle bundle) {
            linkExtras.putAll(bundle);
        }

        private void appendExtra(String key, Object value) {

            if (value instanceof Integer) {
                linkExtras.putInt(key, (Integer) value);
            } else if (value instanceof String) {
                linkExtras.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                linkExtras.putBoolean(key, (Boolean) value);
            } else if (value instanceof Parcelable) {
                linkExtras.putParcelable(key, (Parcelable) value);
            } else if (value instanceof Serializable) {
                linkExtras.putSerializable(key, (Serializable) value);
            }
        }

        private void appendParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
            linkExtras.putParcelableArrayList(key, value);
        }
    }
}