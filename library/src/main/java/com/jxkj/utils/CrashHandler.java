package com.jxkj.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Desc:
 * Author:zhujb
 * Date:2020/5/6
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private static CrashHandler CrashHandler;
    private final int MAX_LOG = 50;

    public static synchronized CrashHandler getInstance() {
        if (CrashHandler == null)
            CrashHandler = new CrashHandler();
        return CrashHandler;
    }
   public CrashHandler context(Context context){
        this.mContext = context;
        return this;
   }
    /**
     * 初始化
     *
     * @param context
     */
    public CrashHandler init(Context context) {
        context(context);
        //系统默认处理类
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该类为系统默认处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
        checkLogFile();
        return this;
    }

    /**
     * 检查现有崩溃日志，如果大于{@link #MAX_LOG}
     * 则按日期排序后清除多出的较早日期日志文件
     */
    private void checkLogFile() {
        File crashLogCacheDirPath = getCrashLogCacheDirPath();
        File[] crashLogFiles = crashLogCacheDirPath.listFiles((dir, name) -> name.startsWith("crash-") && name.endsWith(".log"));
        DateFormat dateFormat = getDateFormat();
        if (crashLogFiles != null && crashLogFiles.length > MAX_LOG) {
            Arrays.sort(crashLogFiles, (o1, o2) -> {
                int result;
                try {
                    Date before = dateFormat.parse(o1.getName().replace("crash-", "").replace(".log", ""));
                    Date after = dateFormat.parse(o2.getName().replace("crash-", "").replace(".log", ""));
                    result = before.after(after) ? 1 : -1;
                } catch (Exception e) {
                    result = 1;
                }
                return result;
            });
            for (int i = 0; i < crashLogFiles.length - MAX_LOG; i++) {
                crashLogFiles[i].delete();
            }
        }
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleExample(e) && mDefaultHandler != null) { //判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(1500);
            }catch ( InterruptedException ignored){
            }
            Log.e("","",e);
            if (crashCallBack != null) {
                crashCallBack.onCatchCrash();
            } else {
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }

    /**
     * 提示用户出现异常
     * 将异常信息保存
     *
     * @param ex
     * @return
     */
    private boolean handleExample(Throwable ex) {
        if (ex == null)
            return false;

        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();
        printLog(ex);
        saveCrashInfoToFile(ex);
        return true;
    }


    private void printLog(Throwable ex){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Log.e("ERROR",writer.toString());
    }
    /**
     * 保存错误信息到文件中
     *
     * @param ex
     */
    public void saveCrashInfoToFile(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable exCause = ex.getCause();
        while (exCause != null) {
            exCause.printStackTrace(printWriter);
            exCause = exCause.getCause();
        }
        printWriter.close();
        //错误日志文件名称
        DateFormat dateFormat = getDateFormat();
        String fileName = "crash-" + dateFormat.format(new Date()) + ".log";
        File newLogFile = new File(getCrashLogCacheDirPath(), fileName);
        try {
            if (!newLogFile.exists()) newLogFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(newLogFile);
            fileOutputStream.write("app version:".getBytes());
            long appVersion = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                appVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).getLongVersionCode();
            } else {
                appVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            }

            fileOutputStream.write(String.valueOf(appVersion).getBytes());
            fileOutputStream.write("\nphone model：".getBytes());

            String deviceInfo = android.os.Build.MODEL;
            fileOutputStream.write((deviceInfo + "\n").getBytes());
            fileOutputStream.write(writer.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
    }

    private File getCrashLogCacheDirPath() {
        File externalFilesDir = mContext.getExternalFilesDir(null);
        //创建日志存放目录
        File logDir = new File(externalFilesDir, "crash_logInfo");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return logDir;
    }

    private CatchCrashCallBack crashCallBack;

    public CrashHandler setCrashCallBack(CatchCrashCallBack crashCallBack) {
        this.crashCallBack = crashCallBack;
        return this;
    }

    public interface CatchCrashCallBack {
        void onCatchCrash();
    }

}

