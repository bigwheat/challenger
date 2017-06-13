package com.tqmall.mvp.model.cache;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.global.Result;
import com.tqmall.mvp.model.user.UserBO;
import com.tqmall.mvp.model.api.UserInfoApi;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/5.
 */

public class UserCache {

    public SharedPreferences sharedPreferences;
    public static UserCache userCache;

    public static UserCache getIns() {
        if (userCache == null) {
            userCache = new UserCache();
        }
        return userCache;
    }

    public UserCache() {
        sharedPreferences = App.getIns().getUserSharedPreferences();
    }

    /**
     * 获得用户信息并缓存
     *
     * @param callback
     */
    public void loadUserInfo(final Runnable callback) {

        H.getNet(UserInfoApi.class).getUserInfo(new Callback<Result<UserBO>>() {

            @Override
            public void success(Result<UserBO> userBOResult, Response response) {
                if (userBOResult.success) {
                    UserBO userBO = userBOResult.data;
                    saveUserInfo(userBO);
                    callback.run();
                } else {
                    Logs.i(userBOResult.code, userBOResult.message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e(error.getUrl(), error.getMessage());
                callback.run();
            }
        });
    }

    /**
     * 存入缓存
     *
     * @param userBO
     */
    private void saveUserInfo(UserBO userBO) {
        if (userBO == null) {
            return;
        }
        String jsonStr = new Gson().toJson(userBO);
        sharedPreferences.edit().putString(Constant.USER_INFO, jsonStr).commit();
    }

    /**
     * 取出缓存中的信息
     *
     * @return
     */
    public UserBO getUserInfo() {
        String jsonStr = sharedPreferences.getString(Constant.USER_INFO, null);
        if (!TextUtils.isEmpty(jsonStr)) {
            UserBO userBO = new Gson().fromJson(jsonStr, UserBO.class);
            return userBO;
        }else {
            return null;
        }
    }

    /**
     * 清空用户信息UserBo
     */
    public void logout() {
        sharedPreferences.edit().putString(Constant.USER_INFO,null).commit();
        H.clearCookies();
        userCache = new UserCache();
    }

    /**
     * 获取userID
     *
     * @return
     */
    public Long getUserId() {
        UserBO user = getUserInfo();
        if (null != user) {
            return user.userId;
        } else {
            return null;
        }
    }

    /**
     * 获取warehouseId
     *
     * @return
     */
    public Integer getWareHouseId() {
        if (null != getUserInfo()) {
            return getUserInfo().warehouseId;
        }
        return null;
    }

}
