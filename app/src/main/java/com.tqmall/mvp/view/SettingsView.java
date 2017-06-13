package com.tqmall.mvp.view;

import com.tqmall.mvp.model.user.UserCenterBo;
import com.tqmall.mvp.view.base.MvpView;

/**
 * Created by Jay on 16/12/8.
 */

public interface SettingsView extends MvpView {

        void initData(UserCenterBo userCenterBo);
}
