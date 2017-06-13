package com.tqmall.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.mvp.activity.base.BaseActivity;

/**
 * Created by Jay on 16/7/28.
 */

public class Alert {

    public static boolean show = true;

    private Alert() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void Msgshort(Context context, String msg) {
        if (show)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void Msglong(Context context, String msg) {
        if (show)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void toast(String info) {
        if (null != BaseActivity.mAct && BaseActivity.mAct.running()) {
            Toast.makeText(BaseActivity.mAct, info, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(App.getIns(), info, Toast.LENGTH_SHORT).show();
        }
        Logs.d("toast:" , info);
    }

    /**
     * 带取消、确定、标题的对话框
     *
     * @param context
     * @param msg
     * @param title
     * @param yes
     * @param no
     */
    public static void alert(Context context, String msg, String title
            , DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no) {

        if(BaseActivity.mAct!=null && BaseActivity.mAct.running){
            new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("确定", yes)
                    .setNegativeButton("取消", no)
                    .show();
        }
    }

    /**
     * 单选确认按钮  带标题 信息 确定按钮
     *
     * @param context
     * @param msg
     * @param title
     * @param yes
     */
    public static void confirm(Context context, String msg, String title
            , DialogInterface.OnClickListener yes) {
        new AlertDialog.Builder(context,R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", yes)
                .show();
    }

    /**
     * 没有按钮的对话框
     *
     * @param context
     * @param msg
     * @param title
     */
    public static void confirm(Context context, String msg, String title) {
        new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                .setTitle(title)
                .setMessage(msg)
                .show();
    }

    /**
     * progressDialog
     *
     * @param activity
     * @param hintText
     * @return
     */
    public static ProgressDialog progress(Activity activity, String hintText) {
        return progress(activity, hintText, false);
    }

    /**
     * progressDialog
     *
     * @param activity
     * @param hintText
     * @param cancelable
     * @return
     */
    public static ProgressDialog progress(Activity activity, String hintText, boolean cancelable) {
        Activity mActivity;
        if (activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        if (finalActivity.isFinishing()) {
            return null;
        }
        ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);
        window.setCancelable(cancelable);
        window.show();
        return window;
    }


}
