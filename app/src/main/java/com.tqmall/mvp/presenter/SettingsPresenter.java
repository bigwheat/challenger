package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 16/12/14.
 */

public interface SettingsPresenter extends MvpPresenter {

    /**
     * 退出登录
     */
    void logout();

    void initData();

}
