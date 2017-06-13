package com.tqmall.mvp.presenter.base.impl;

import android.support.annotation.NonNull;
import com.tqmall.mvp.presenter.base.RequestCallBack;
import com.tqmall.mvp.presenter.base.MvpPresenter;
import com.tqmall.mvp.view.base.MvpView;

import javax.inject.Inject;

/**
 * Created by Jay on 16/11/29.
 */

public class MvpPresenterImpl<V extends MvpView> implements MvpPresenter {

    protected V mView;

    /**
     * 打开连接,使P持有view对象
     *
     * @param view
     */
    @Override
    public void attachView(@NonNull MvpView view) {
        mView = (V) view;
    }

    /**
     * 关闭P层与View连接
     */
    @Override
    public void detachView() {
        mView=null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }
}

