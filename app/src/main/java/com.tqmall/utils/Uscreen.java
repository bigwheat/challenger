package com.tqmall.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Jay on 16/7/29.
 */

public class Uscreen {
    private Uscreen() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取屏幕高度;
     *
     * @param context
     */
    public static int getScreenHight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕高度;
     *
     * @param context
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 截取屏幕包括状态栏;
     *
     * @param activity
     */
    public static Bitmap snapShotScreen(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int hight = getScreenHight(activity);
        Bitmap map = null;
        map = Bitmap.createBitmap(bitmap, 0, 0, width, hight);
        view.destroyDrawingCache();
        return map;
    }
}
