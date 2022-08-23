/*
 *     (C) Copyright 2019, ForgetSky.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.jxkj.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;

public class CommonUtils {

    public static AlertDialog getLoadingDialog(Context context, String message) {

        View view = LayoutInflater.from(context).inflate(R.layout.loading_progressbar, null, false);
        TextView loadingText = view.findViewById(R.id.loading_text);
        if (!TextUtils.isEmpty(message)) {
            loadingText.setVisibility(View.VISIBLE);
            loadingText.setText(message);
        }
        AlertDialog dialog = new AlertDialog.Builder(context).setCancelable(true).setView(view).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
