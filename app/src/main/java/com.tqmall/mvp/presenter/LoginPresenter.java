package com.tqmall.mvp.presenter;


import com.tqmall.mvp.presenter.base.MvpPresenter;
import com.tqmall.utils.callback.ResultCallback;

/**
 * Created by Jay on 16/11/29.
 */

public interface LoginPresenter extends MvpPresenter{

    /**
     * 用户登录
     *
     * @param userName
     * @param userPassword
     * @param remember
     * @param callback
     * @return
     */
    void login(String userName, String userPassword, boolean remember, final ResultCallback callback);


}
