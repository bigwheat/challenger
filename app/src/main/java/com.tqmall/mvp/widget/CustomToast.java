package com.tqmall.mvp.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tqmall.R;

/**
 * Created by Jay on 17/1/6.
 */

public class CustomToast {

    private static TextView mTextView;

    private static ImageView mImageView;

    public static final int ERROR_STATUS = -1;

    public static final int WARN_STATUS = 0;

    public static final int SUCCESS_STATUS = 1;

    public static void showToast(Context context, String message, int status) {

        View toastRoot = LayoutInflater.from(context).inflate(R.layout.custom_toast_transfer_success, null);
        mTextView = (TextView) toastRoot.findViewById(R.id.message);
        mTextView.setText(message);
        mImageView = (ImageView) toastRoot.findViewById(R.id.imageView);
        switch (status) {
            case -1:
                break;
            case 0:
                mImageView.setImageResource(R.mipmap.warn_white);
                break;
            case 1:
                mImageView.setImageResource(R.mipmap.stock_in_success);
                break;
            default:
                break;
        }
        Toast toastStart = new Toast(context);
        LinearLayout layout2 = (LinearLayout) toastRoot.findViewById(R.id.child);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        layout2.getLayoutParams().width = width / 2;
        toastStart.setGravity(Gravity.CENTER, 0, 0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void showToastInBottom(Context context, String message) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.custom_toast_buttom, null);
        mTextView = (TextView) toastRoot.findViewById(R.id.message);
        mTextView.setText(message);
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        toastStart.setGravity(Gravity.BOTTOM, 0, height / 15);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
