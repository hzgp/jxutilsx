package com.jxkj.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by admin on 2018/8/15.
 */

public class OSUtils {

    private static final String TAG = "Rom";
    public static final String CUP_ABI_X86 = "x86";

    public static final String ROM_MIUI = "MIUI";
    public static final String ROM_EMUI = "EMUI";
    public static final String ROM_FLYME = "FLYME";
    public static final String ROM_OPPO = "OPPO";
    public static final String ROM_SMARTISAN = "SMARTISAN";
    public static final String ROM_VIVO = "VIVO";
    public static final String ROM_QIKU = "QIKU";

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
    private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
    private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";

    private static String sName;
    private static String sVersion;

    public final static int INTERNET_STATE_NON = 0;
    public final static int INTERNET_STATE_WIFI = 1;
    public final static int INTERNET_STATE_CELLULAR = 2;


    public static boolean isEmui() {
        return check(ROM_EMUI);
    }

    public static boolean isMiui() {
        return check(ROM_MIUI);
    }

    public static boolean isVivo() {
        return check(ROM_VIVO);
    }

    public static boolean isOppo() {
        return check(ROM_OPPO);
    }

    public static boolean isFlyme() {
        return check(ROM_FLYME);
    }

    public static boolean isSamSung() {
        return Build.MODEL.toLowerCase().contains("samsung") || Build.MODEL.toLowerCase().contains("galaxy");
    }

    public static boolean is360() {
        return check(ROM_QIKU) || check("360");
    }

    public static boolean isSmartisan() {
        return check(ROM_SMARTISAN);
    }

    public static String getName() {
        if (sName == null) {
            check("");
        }
        return sName;
    }

    public static String getVersion() {
        if (sVersion == null) {
            check("");
        }
        return sVersion;
    }

    public static boolean check(String rom) {
        if (sName != null) {
            return sName.equals(rom);
        }

        if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
            sName = ROM_MIUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
            sName = ROM_EMUI;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
            sName = ROM_OPPO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
            sName = ROM_VIVO;
        } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
            sName = ROM_SMARTISAN;
        } else {
            sVersion = Build.DISPLAY;
            if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                sName = ROM_FLYME;
            } else {
                sVersion = Build.UNKNOWN;
                sName = Build.MANUFACTURER.toUpperCase();
            }
        }
        return sName.equals(rom);
    }

    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read prop " + name, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    /**
     * @return
     */
    public static int getVersionCode(Context context) {
        // 包管理器 可以获取清单文件信息
        PackageManager packageManager = context.getPackageManager();
        try {
            // 获取包信息
            // 参1 包名 参2 获取额外信息的flag 不需要的话 写0
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取版本信息
     */
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取CUP架构类型
     *
     * @return
     */
    public static String getCPUABI() {
        String CPUABI;
        try {
            InputStream in = Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String os_cpuabi = bufferedReader.readLine();
            if (os_cpuabi.contains("x86")) {
                CPUABI = "x86";
            } else if (os_cpuabi.contains("arm64-v8a")) {
                CPUABI = "arm64-v8a";
            } else if (os_cpuabi.contains("armeabi-v7a")) {
                CPUABI = "armeabi-v7a";
            } else {
                CPUABI = "armeabi";
            }
            in.close();
            bufferedReader.close();
        } catch (Exception e) {
            CPUABI = "armeabi";
        }
        return CPUABI;
    }

    /**
     * 判断是否在主进程,这个方法判断进程名或者pid都可以,如果进程名一样那pid肯定也一样
     *
     * @param context
     * @return true:当前进程是主进程 false:当前进程不是主进程
     */
    public static boolean isUIProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断程序是否在后台
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }

    /**
     * 判断CPU架构是否是x86
     *
     * @param cpuAbi 带判断的类型
     * @return 如果是返回true, 反之为false
     */
    public static boolean isABI_X86(String cpuAbi) {
        return CUP_ABI_X86.equals(cpuAbi);
    }

    public static int networkState(Context context) {
        //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        if (manager == null) {
            return INTERNET_STATE_NON;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network == null) {
                return INTERNET_STATE_NON;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return INTERNET_STATE_NON;
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return INTERNET_STATE_WIFI;
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                return INTERNET_STATE_CELLULAR;

            return INTERNET_STATE_NON;
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                return INTERNET_STATE_NON;
            }

            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            int networkType = networkInfo.getType();
            if (networkType == ConnectivityManager.TYPE_MOBILE)
                return INTERNET_STATE_CELLULAR;
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                return INTERNET_STATE_WIFI;
            }
            return INTERNET_STATE_NON;
        }
    }

    /**
     * 判断是否为小米设备
     */
    public static boolean isBrandXiaoMi() {
        String buildBrand= getBuildBrand();
        String buildManufacturer=getBuildManufacturer();
        return ("xiaomi".equalsIgnoreCase(buildBrand) || "xiaomi".equalsIgnoreCase(buildManufacturer));
    }

    /**
     * 判断是否为华为设备
     */
    public static boolean isBrandHuawei() {
        String buildBrand= getBuildBrand();
        String buildManufacturer=getBuildManufacturer();
        return "huawei".equalsIgnoreCase(buildBrand) ||
                "huawei".equalsIgnoreCase(buildManufacturer) ||
                "honor".equalsIgnoreCase(buildBrand) ||
                "honor".equalsIgnoreCase(buildManufacturer);
    }

    /**
     * 判断是否为魅族设备
     */
    public static boolean isBrandMeizu() {
        String buildBrand= getBuildBrand();
        String buildManufacturer=getBuildManufacturer();
        return ("meizu".equalsIgnoreCase(buildBrand)
                || "meizu".equalsIgnoreCase(buildManufacturer)
                || "22c4185e".equalsIgnoreCase(buildBrand));
    }

    /**
     * 判断是否是 oppo 设备, 包含子品牌
     *
     */
    public static boolean isBrandOppo() {
        String buildBrand= getBuildBrand();
        String buildManufacturer=getBuildManufacturer();
        return "oppo".equalsIgnoreCase(buildBrand) ||
                "realme".equalsIgnoreCase(buildBrand) ||
                "oneplus".equalsIgnoreCase(buildBrand) ||
                "oppo".equalsIgnoreCase(buildManufacturer) ||
                "realme".equalsIgnoreCase(buildManufacturer) ||
                "oneplus".equalsIgnoreCase(buildManufacturer);
    }

    /**
     * 判断是否是vivo设备
     *
     */
    public static boolean isBrandVivo() {
        String buildBrand= getBuildBrand();
        String buildManufacturer=getBuildManufacturer();
        return ("vivo".equalsIgnoreCase(buildBrand) || "vivo".equalsIgnoreCase(buildManufacturer));
    }

    private static String BRAND = "";
    private static String MANUFACTURER = "";

    private static String getBuildBrand() {
        if (BRAND == null || BRAND.isEmpty()) {
            synchronized (OSUtils.class) {
                if (BRAND == null || BRAND.isEmpty()) {
                    BRAND = Build.BRAND;
                    Log.i(TAG, "get BRAND by Build.BRAND :" + BRAND);
                }
            }
        }

        return BRAND;
    }

    private static String getBuildManufacturer() {
        if (MANUFACTURER == null || MANUFACTURER.isEmpty()) {
            synchronized (OSUtils.class) {
                if (MANUFACTURER == null || MANUFACTURER.isEmpty()) {
                    MANUFACTURER = Build.MANUFACTURER;
                    Log.i(TAG, "get MANUFACTURER by Build.MANUFACTURER :" + MANUFACTURER);
                }
            }
        }

        return MANUFACTURER;
    }
}