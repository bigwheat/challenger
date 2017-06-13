package com.tqmall.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

import com.tqmall.global.App;

/**
 * Created by Jay on 16/7/28.
 */

public class Logs {

    private Logs() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String tag, String msg) {
        if (isApkDebug(App.mContext) && !TextUtils.isEmpty(msg)) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isApkDebug(App.mContext) && !TextUtils.isEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isApkDebug(App.mContext) && !TextUtils.isEmpty(msg)) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isApkDebug(App.mContext) && !TextUtils.isEmpty(msg)) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isApkDebug(App.mContext) && !TextUtils.isEmpty(msg)) {
            Log.w(tag, msg);
        }
    }

    public static boolean isApkDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }
}
