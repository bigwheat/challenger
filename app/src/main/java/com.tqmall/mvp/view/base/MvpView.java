package com.tqmall.mvp.view.base;


/**
 * Created by Jay on 16/11/29.
 */

public interface MvpView {

    void showProgress(String message);

    void hideProgress();

    void showMsg(String message);

    void showToastMsg(String message,int status);

}
