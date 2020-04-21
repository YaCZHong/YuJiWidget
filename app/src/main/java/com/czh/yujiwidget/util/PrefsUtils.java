package com.czh.yujiwidget.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PrefsUtils {

    public static void putString(Context context, String key, String info) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, info);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        String Info;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Info = prefs.getString(key, null);
        return Info;
    }

    public static void putBoolean(Context context, String key, boolean info) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, info);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        boolean Info;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Info = prefs.getBoolean(key, false);
        return Info;
    }

    public static void remove(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
