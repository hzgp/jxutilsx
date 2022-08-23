package com.jxkj.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by DY on 2017/10/10.
 */

public class ToastUtils {

    private static Toast toast;

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        LayoutInflater inflate = LayoutInflater.from(applicationContext);
        View view = inflate.inflate(R.layout.lay_toast, null);
        TextView txt = view.findViewById(R.id.tv_msg);
        txt.setText(message);
        //防止多次点击按钮出现很多toast一直不消失
        if (toast != null) {
            toast.setView(view);
        } else {
            toast = new Toast(applicationContext);
            toast.setView(view);
            //toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        try {
            toast.show();
        } catch (Exception ignore) {
        }
    }
}