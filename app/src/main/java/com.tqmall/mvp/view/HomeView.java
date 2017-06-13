package com.tqmall.mvp.view;

import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 16/12/8.
 */

public interface HomeView extends MvpView{

    /**
     * 初始化用户菜单权限
     *
     * @param userRole
     */
    void initPermissionInActivity(List<String> userRole);

}
