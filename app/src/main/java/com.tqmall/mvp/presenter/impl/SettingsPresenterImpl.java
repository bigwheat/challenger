package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.App;
import com.tqmall.global.Result;
import com.tqmall.mvp.model.api.UserInfoApi;
import com.tqmall.mvp.model.cache.UserCache;
import com.tqmall.mvp.model.user.UserCenterBo;
import com.tqmall.mvp.presenter.SettingsPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.SettingsView;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/14.
 */

public class SettingsPresenterImpl extends MvpPresenterImpl<SettingsView> implements SettingsPresenter {


    @Inject
    public SettingsPresenterImpl (){

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void logout() {

        H.getNet(UserInfoApi.class).logOut(UserCache.getIns().getUserId().toString(),
                UserCache.getIns().getWareHouseId().toString().trim(),
                new Callback<Result<String>>() {
            @Override
            public void success(Result<String> stringResult, Response response) {
                if(stringResult.success){
//                    Logs.d("成功退出登录:{}",stringResult.data);
                    mView.showMsg("退出成功");
                    App.getIns().onTerminate();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("logout():{}",error.toString());
            }
        });
    }


    @Override
    public void initData() {
        H.getNet(UserInfoApi.class).getUserCenterInfo(new Callback<Result<UserCenterBo>>() {
            @Override
            public void success(Result<UserCenterBo> userCenterBoResult, Response response) {
                if(userCenterBoResult.success&&userCenterBoResult.data!=null){
                    if(mView!=null){
                        mView.initData(userCenterBoResult.data);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Logs.e("getUserCenterInfo():{}",error.toString());
            }
        });
    }
}
