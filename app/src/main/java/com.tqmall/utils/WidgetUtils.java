package com.tqmall.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jay on 17/1/14.
 */

public class WidgetUtils {

    public static final String BATCH = "BATCHO";

    public static final String ALLOCATION = "ALLOCATIONSN";

    public static final String NULL = "NULL";


    private WidgetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 为EditText设置输入法
     * 延时500s防止因为页面加载未完成引起无法弹出的问题
     *
     * @param editText
     */
    public static void setEditTextInputMode(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 500);
    }

    /**
     * 隐藏输入法
     *
     * @param editText
     */
    public static void hideEditTextInputMode(EditText editText){
        ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 判断扫码返回类型
     * 批次码  库位码  or  未知类型NULL
     *
     * @param code
     * @return
     */
    public static String scanCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            if (code.contains("-")) {
                return ALLOCATION;
            } else {
                return BATCH;
            }
        }
        return NULL;
    }
}
