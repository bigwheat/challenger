package com.tqmall.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tqmall.global.App;
import com.tqmall.utils.zxing.activity.CaptureActivity;

import java.io.Serializable;

/**
 * Created by Jay on 16/8/4.
 */

public class IntentUtils{


    private IntentUtils() {
        throw new UnsupportedOperationException("Can not be instantiated");
    }

    /**
     * Activity跳转,无参数传递,跳转完成finish掉
     *
     * @param activity
     * @param cla
     */
    public static void launch(Activity activity, Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(activity, cla);
        activity.startActivity(intent);
        activity.finish();
    }
    /**
     * Activity跳转,无参数传递,跳转完成不finish掉
     *
     * @param activity
     * @param cla
     */
    public static void launchNoFinish(Activity activity, Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(activity, cla);
        activity.startActivity(intent);
    }

    /**
     * Activity跳转,参数传递,跳转完成finish掉
     *
     * @param activity
     * @param cla
     * @param object
     * @param tag
     */
    public static void launchNoFinish(Activity activity, Class<?> cla, Object object, String tag) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setClass(activity, cla);
        if (object instanceof Integer) {
            bundle.putInt(tag, (int) object);
        } else if (object instanceof Long) {
            bundle.putLong(tag, (long) object);
        } else if (object instanceof String) {
            bundle.putString(tag, (String) object);
        } else if (object instanceof Boolean) {
            bundle.putBoolean(tag, (boolean) object);
        } else if (object instanceof Double) {
            bundle.putDouble(tag, (double) object);
        }
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    /**
     * Activity跳转,参数传递,跳转完成finish掉
     *
     * @param activity
     * @param cla
     * @param key
     * @param obj
     */
    public static void launchWithObjectNoFinish(Activity activity, Class<?> cla, String key,Serializable obj) {
        Intent intent = new Intent();
        intent.putExtra(key,obj);
        intent.setClass(activity, cla);
        activity.startActivity(intent);
    }

    /**
     * Activity跳转  Intent.FLAG_ACTIVITY_CLEAR_TOP
     *
     * @param activity
     * @param clazz
     * @param front
     */
    public static void launchClearTop(Activity activity,Class<?> clazz,Boolean front) {
        if (!front) {
            Intent intent = new Intent(activity,clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }
    }

    /**
     * Activity跳转  Intent.FLAG_ACTIVITY_CLEAR_TOP
     *
     * @param activity
     * @param clazz
     */
    public static void launchClearTopWithFinish(Activity activity,Class<?> clazz) {
            Intent intent = new Intent(activity,clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
    }
}
