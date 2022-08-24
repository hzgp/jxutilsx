package com.jxkj.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

/**
 * Desc:
 * Author:zhujb
 * Date:2020/5/26
 */
public class JXConfig {

    private DemoDbConfig demoDbConfig;
    private PushConfig pushConfig;
    private CrashReportConfig crashReportConfig;
    private IConfigLoader configLoader;
    private static JXConfig instance;

    private JXConfig() {
    }

    public static synchronized JXConfig getInstance() {
        if (instance == null) {
            throw new NullPointerException("please call init() first");
        }
        return instance;
    }

    public static JXConfig init(String buildConfigClassName, IConfigLoader configLoader) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        instance = new JXConfig();
        Class buildConfigCls = Class.forName(buildConfigClassName);
        Object buildConfig = buildConfigCls.newInstance();
        instance.configLoader = configLoader;
        instance.initDemoDbConfig(buildConfigCls, buildConfig);
        instance.initPushConfig();
        instance.initCrashReportConfig(buildConfigCls, buildConfig);
        return instance;
    }

    public static JXConfig init( IConfigLoader configLoader){
        instance = new JXConfig();
        instance.configLoader = configLoader;
        return instance;
    }
    private void initCrashReportConfig(Class buildConfigCls, Object target) {
        crashReportConfig = new CrashReportConfig();
        try {
            crashReportConfig.ALiAppKey = configLoader.ALi_App_Key();
            crashReportConfig.ALiAppSecret = configLoader.ALi_App_Secret();
            crashReportConfig.crashHandleMode = getFieldValueByName(buildConfigCls, "CrashHandleMode", target);
            crashReportConfig.channelId = getFieldValueByName(buildConfigCls, "channelId", target);
        } catch (Exception e) {
            pushConfig = null;
        }
    }

    private void initPushConfig() {
        pushConfig = new PushConfig();
        try {
            pushConfig.MeiZuAppId = configLoader.MeiZu_App_Id();
            pushConfig.MeiZuAppKey = configLoader.MeiZu_App_Key();
            pushConfig.MiPushAppId = configLoader.MiPush_App_Id();
            pushConfig.MiPushAppKey = configLoader.MiPush_App_Key();
            pushConfig.OppoPushAppKey = configLoader.OppoPush_App_Key();
            pushConfig.OppoPushAppSecret = configLoader.OppoPush_App_Secret();
        } catch (Exception e) {
            pushConfig = null;
        }
    }

    private void initDemoDbConfig(Class buildConfigCls, Object target) {
        demoDbConfig = new DemoDbConfig();
        try {
            demoDbConfig.chatActivityClass = (Class<? extends Activity>) Class.forName((String) buildConfigCls.getDeclaredField("ChatActivity").get(target));
            demoDbConfig.mainActivityClass = (Class<? extends Activity>) Class.forName((String) buildConfigCls.getDeclaredField("MainActivity").get(target));
        } catch (Exception e) {
            demoDbConfig = null;
        }
    }

    public String getFieldValueByName(Class buildConfigCls, String fieldName, Object target) {
        String value = null;
        try {
            value = (String) buildConfigCls.getDeclaredField(fieldName).get(target);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException ignored) {
        }
        return value;
    }

    public IConfigLoader getConfigLoader() {
        return configLoader;
    }

    public DemoDbConfig getDemoDbConfig() {
        return demoDbConfig;
    }

    public CrashReportConfig getCrashReportConfig() {
        return crashReportConfig;
    }

    public PushConfig getPushConfig() {
        return pushConfig;
    }

    public boolean isDemoDbConfigAvailable() {
        return isPushConfigAvailable() && demoDbConfig != null;
    }

    public boolean isPushConfigAvailable() {
        return pushConfig != null;
    }

    public static class DemoDbConfig {
        public Class<? extends Activity> chatActivityClass;
        public Class<? extends Activity> mainActivityClass;
    }

    public static class PushConfig {
        public String MeiZuAppKey;
        public String MeiZuAppId;
        public String MiPushAppKey;
        public String MiPushAppId;
        public String OppoPushAppKey;
        public String OppoPushAppSecret;

    }

    public static class CrashReportConfig {
        public static final String CRASH_LOCAL = "0";
        public static final String CRASH_ALI_Report = "1";
        public static final String CRASH_ALL = "2";
        public String ALiAppKey;
        public String ALiAppSecret;
        public String channelId;
        private String crashHandleMode;  //0.启用本地日志保存 1.启用阿里崩溃分析 2.阿里崩溃分析及本地日志保存 (默认)

        public String getCrashHandleMode() {
            return TextUtils.isEmpty(crashHandleMode) ? CRASH_ALL : crashHandleMode;
        }
    }


}
