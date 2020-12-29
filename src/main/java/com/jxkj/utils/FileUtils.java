package com.jxkj.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by myc on 2016/12/14.
 * More Code on 1101255053@qq.com
 * Description:
 */
public class FileUtils {
    public static boolean fileIsExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
    public static String APP_ROOT_PATH;

    public static File getRootDir(Context context){
        File appRootDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appRootDir = context.getExternalFilesDir(null);
            if (appRootDir == null) {
                return null;
            }
            appRootDir = getRootDir_VERSION_M_UP(appRootDir);
        } else {
            appRootDir = Environment.getExternalStorageDirectory();
            if (appRootDir == null) {
                return null;
            }
        }
       return appRootDir;
    }

    public static void initAppRootPath(Context context, String appRootDirName) {
        File appRootDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appRootDir = context.getExternalFilesDir(null);
            if (appRootDir == null) {
                return;
            }
            appRootDir = getRootDir_VERSION_M_UP(appRootDir);
        } else {
            appRootDir = Environment.getExternalStorageDirectory();
            if (appRootDir == null) {
                return;
            }
        }
        APP_ROOT_PATH = appRootDir == null ? null : appRootDir.getAbsolutePath() + File.separator + appRootDirName;
    }
    private static File getRootDir_VERSION_M_UP(File externalFilesDir) {
        if (externalFilesDir.getParentFile() == null) {
            return null;
        } else if (externalFilesDir.getName().equals("Android")) {
            return externalFilesDir.getParentFile();
        } else {
            return getRootDir_VERSION_M_UP(externalFilesDir.getParentFile());
        }
    }
}
