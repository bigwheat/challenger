package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

import java.util.List;

/**
 * Created by Jay on 16/12/9.
 */

public interface HomePresenter extends MvpPresenter {

    /**
     * 加载用户信息,获取用户获取菜单权限
     */
    void loadUserInfo();


    /**
     * 初始化权限信息
     *
     * @param list
     */
    void initPermission(List<String> list);
}
