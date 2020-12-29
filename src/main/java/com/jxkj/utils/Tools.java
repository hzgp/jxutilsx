package com.jxkj.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.ClipboardManager;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.core.content.FileProvider;


public class Tools {


    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static String getJson(Map<String, Object> paramMap) {

        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    /**
     * 获取屏幕的宽度
     */

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static int dp2px(Context context, float dp) {
        return (int) (getDensity(context) * dp + 0.5f);
    }

    public static int[] dp2px(Context context, float... dps) {
        float scale = getDensity(context);
        int[] result = new int[dps.length];
        int i = 0;
        for (float dp : dps) {
            result[i] = (int) (scale * dp + 0.5f);
            i++;
        }
        return result;
    }

    public static int[] px2dp(Context context, float... pxs) {
        float scale = getDensity(context);
        int []result = new int[pxs.length];
        int i=0;
        for (float px:pxs){
            result[i] =(int) (px / scale + 0.5f);
            i++;
        }
        return  result;
    }
    public static int px2dp(Context context, float px) {
        return (int) (px / getDensity(context) + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        return (int) (getFontDensity(context) * sp + 0.5f);
    }

    public static int px2sp(Context context, float px) {
        return (int) (px / getFontDensity(context) + 0.5f);
    }


    private static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    private static float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public static void doSendSMSTo(final Activity activity, String phoneNumber,
                                   String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                    + phoneNumber));
            intent.putExtra("sms_body", message);
            activity.startActivity(intent);
        }
    }

    /**
     * 打电话
     *
     * @param context
     * @param callPhone
     */
    public static void callPhone(Context context, String callPhone) {
        if (!"".equals(callPhone) && callPhone != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                    + callPhone));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "没有获取到电话，无法拨打", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 毫秒转时间格式
     */
    public static String toTime(long time) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }
    /**
     * 毫秒转时间格式
     */
    public static String getStringDateTime(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static byte[] bitmaptobytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     * 视频中取图片
     *
     * @param activity
     * @param uri
     * @return
     */
    public static Bitmap getVideoThumbnail(Activity activity, Uri uri) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(activity, uri);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    /**
     * 信任所有主机-对于任何证书都不做检查
     */
    @SuppressLint("TrulyRandom")
    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android 采用X509的证书信息机制
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }


            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                           String authType) throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }


            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                           String authType) throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub

            }
        }};


        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     *
     * @param strText
     * @return
     */
    public static String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

    /**
     * 字符串 SHA 加密
     *
     * @param strText
     * @return
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String imgPath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 如果该值设为true那么将不返回实际的bitmap对象，不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息
        BitmapFactory.decodeFile(imgPath, options); // 解码文件
        // 计算缩放值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 设置好缩放值之后，inJustDecodeBounds设为false，就可以根据已经得到的缩放比例得到自己想要的图片缩放图
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Textview 跑马灯效果
     *
     * @param textView
     */
    public static void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }



    /**
     * 获取TextView高度
     *
     * @param Context
     * @param text
     * @param textSize
     * @return
     */

    public static float getTextWidth(Context Context, String text, int textSize) {
        TextPaint paint = new TextPaint();
        float scaledDensity = Context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return paint.measureText(text);
    }


    public static boolean isQQAvailable(Context context) {
        final PackageManager mPackageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = mPackageManager.getInstalledPackages(0);
        for (PackageInfo info :
                installedPackages) {
            String packageName = info.packageName;
            if (packageName.equals("com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    /**
     *  
     *  * 获取状态栏高度——方法1 
     *  * 
     */
    public static int getstatusBarHeight(Context context) {
        int statusBarHeight1 = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值  
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    //打开或者关闭gps
    public static void openGPS(Activity activity, boolean open) {
        if (Build.VERSION.SDK_INT < 19) {
            Settings.Secure.setLocationProviderEnabled(activity.getContentResolver(),
                    LocationManager.GPS_PROVIDER, open);
        } else {
            if (!open) {
                Settings.Secure.putInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
            } else {
                Settings.Secure.putInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_BATTERY_SAVING);
            }
        }
    }

    //判断gps是否处于打开状态
    public static boolean isOpen(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            LocationManager myLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            return myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            int state = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
            if (state == Settings.Secure.LOCATION_MODE_OFF) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static String getSexName(int sex){
        String sexName = "";
        if(sex == 1){
            sexName = "男";
        }else {
            sexName = "女";
        }
        return sexName;
    }

    //用****替换身份证号码
    public static String hideIdCard(String idCard) {
        String maskNumber = idCard.substring(0, 3) + "*********" + idCard.substring(12, idCard.length());
        return maskNumber;
    }

    //用****替换手机号码中间4位
    public static String hidePhone(String phone) {
        String maskNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        return maskNumber;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = getSDPath() + "/jiuxing/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }

    /**
     * 将double类型数据转换为百分比格式，并保留小数点前IntegerDigits位和小数点后FractionDigits位
     *
     * @param d
     * @param IntegerDigits
     * @param FractionDigits
     * @return
     */
    public static String getPercentFormat(double d, int IntegerDigits, int FractionDigits) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
        nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }

    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }



    /**
     * 分享图片到 微信
     *
     * @param file 本地的图片
     */
    public static void shareWeChat(File file, Context context) {
        Uri uriToImage = FileProvider.getUriForFile(context, "com.fskj.onlinehospital.fileprovider", file);
        //Uri uriToImage = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        //发送图片到朋友圈
        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //发送图片给好友。
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }
    public static void shareToWeChat(File file, Context context) {
        // TODO: 2015/12/13 将需要分享到微信的图片准备好
        try {
            if (!checkInstallation(context, "com.tencent.mm")) {
                ToastUtils.showToast(context, "请先安装微信应用");
                return;
            }
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
//        intent.setType("text/plain");
            //添加Uri图片地址
//        String msg=String.format(getString(R.string.share_content), getString(R.string.app_name), getLatestWeekStatistics() + "");
            ArrayList<Uri> imageUris = new ArrayList<Uri>();
            // TODO: 2016/3/8 根据不同图片来设置分享
            BitmapDrawable bitmapDrawable;
            if (Build.VERSION.SDK_INT < 22) {
                imageUris.add(Uri.fromFile(file));
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUris.add(Uri.fromFile(file));
            } else {
                //修复微信在7.0崩溃的问题
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), "member.jpg", null));
                imageUris.add(uri);
            }

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            ((Activity) context).startActivityForResult(intent, 1000);
        } catch (Throwable e) {
            ToastUtils.showToast(context, "分享失败");
        }
    }


    private static boolean checkInstallation(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public final static String setMd5(String plaintext) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isEmpty(List mList) {
        return mList == null || mList.size() == 0;
    }

    //获取图片缓存地址
    public static String getImagePath() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "/machinerent/cache/images");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir.getAbsolutePath();
    }

    //获取图片缓存地址
    public static String getImageCache() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "/machinerent/cache/images");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File file = new File(appDir, new Date().getTime() + ".jpg");
        return file.getAbsolutePath();
    }

    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            return new Point(display.getWidth(), display.getHeight());
        } else {
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }

    /*使整个屏幕界面变暗*/
    public static void screenDarken(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = 0.7f;//设置整个屏幕的透明度为0.7
        window.setAttributes(attributes);
    }

    /*使整个屏幕界面变正常*/
    public static void screenLight(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = 1f;//设置整个屏幕的透明度为1
        window.setAttributes(attributes);
    }

    public static String getYear(String date) {
        String year = "";
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(date);//定义起始日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.CHINA);
            year = sdf.format(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return year;
    }

    public static String getMonth(String date) {
        String month = "";
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(date);//定义起始日期
            SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
            month = sdf.format(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return month;
    }

    public static String getDay(String date) {
        String day = "";
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(date);//定义起始日期
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.CHINA);
            day = sdf.format(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }

    public static String getTime(String date) {
        String time = "";
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(date);//定义起始日期
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
            time = sdf.format(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String SaveTwo(float price) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(price);
        return dd;
    }

    //当前应用是否处于前台
    public static boolean isForeground(Context context) {
        if (context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
                if (processInfo.processName.equals(context.getPackageName())) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static Bitmap convertViewToBitmap(View view) {
        int w = view.getWidth();
        int h = view.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        //view.layout(0, 0, w, h);
        view.draw(c);
        return bmp;
    }


    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "九星医疗";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getStatueBarHeight(Activity activity) {
        int identifier = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return (int) activity.getResources().getDimension(identifier);
        }
        return 0;
    }

    public static List<Uri> convert(List<String> data) {
        List<Uri> list = new ArrayList<>();
        for (String d : data) list.add(Uri.parse(d));
        return list;
    }

    /** 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDate(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringDateHour(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public static boolean isNotFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static String removeN(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        String str = "";
        str = content.replaceAll("(\\r\\n|\\n\\r|\\r|\\n)", "");
        return str;
    }

    public static String removeBr(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        String str = "";
        str = content.replace("<br/>", "");
        return str;
    }

    public static String changeNtoBr(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        String str = "";
        str = content.replaceAll("(\\r\\n|\\n\\r|\\r|\\n)", "<br/>");
        return str;
    }

    public static String changeBrtoN(String content){
        if(TextUtils.isEmpty(content)){
            return "";
        }
        String str = "";
        str = content.replace("<br/>","\n");
        return str;
    }

    public static String getTopWeekDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - 7);
        Date d = c.getTime();
        return format.format(d);
    }

    public static String getTopMonth(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date d = c.getTime();
        return format.format(d);
    }

    public static String getTop3Month(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -3);
        Date d = c.getTime();
        return format.format(d);
    }

    public static Bitmap getBitmap(Context context,int vectorDrawableId) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }
    /**
     * 输入框不允许输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){

        InputFilter filter = (source, start, end, dest, dStart, dEnd) -> {

            if(source.equals(" ")){
                return "";
            }else{
                return null;
            }
        };

        editText.setFilters(new InputFilter[]{filter});

    }

    /**
     * 输入框不允许换行
     * @param editText
     */
    public static void setEditTextLineFeed(EditText editText){

        editText.setOnEditorActionListener((v, actionId, event) -> (event.getKeyCode()==KeyEvent.KEYCODE_ENTER));

    }
}
