package com.jxkj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreference {

    public static void save(String name, String value, Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "cache", Activity.MODE_PRIVATE);
        Editor editor = mySharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String get(String name, String defvalue, Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "cache", Activity.MODE_PRIVATE);
        return mySharedPreferences.getString(name, defvalue);
    }

    public static void clear(Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "cache", Activity.MODE_PRIVATE);
        Editor editor = mySharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void saveInt(String name, int value, Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "cache", Activity.MODE_PRIVATE);
        mySharedPreferences.edit().putInt(name, value).apply();
    }

    public static int getInt(String name, int defValue, Context context) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "cache", Activity.MODE_PRIVATE);
        return mySharedPreferences.getInt(name, defValue);
    }
}
