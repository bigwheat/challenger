package com.tqmall.mvp.presenter.base;

import android.support.annotation.NonNull;

import com.tqmall.mvp.view.base.MvpView;


/**
 * Created by Jay on 16/11/29.
 */

public interface MvpPresenter {

    /**
     * 初始化,可以进行RxJava 等初始化调用操作
     *
     */
    void onCreate();

    /**
     * onResume()
     */
    void onResume();

    /**
     * 打开Activity与view层的开关
     *
     * @param view
     */
    void attachView(@NonNull MvpView view);

    /**
     * 在Activity进入onDestroy()时,分离Activity持有的view层对象,防止内存泄漏
     *
     */
    void detachView();

}
