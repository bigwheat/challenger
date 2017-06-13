package com.tqmall.mvp.model.api;


import com.tqmall.global.Result;
import com.tqmall.mvp.model.user.Authority;
import com.tqmall.mvp.model.user.UserBO;
import com.tqmall.mvp.model.user.UserCenterBo;
import com.tqmall.mvp.model.user.UserCommon;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Created by Jay on 16/8/30.
 */

public interface UserInfoApi {


    @GET("/pvg/user/currUserInfo")
    void getUserCenterInfo(Callback<Result<UserCenterBo>> callback);

    /**
     * 获取用户信息
     */
    @GET("/router/user_manage/get_user_info")
    void getUserInfo( Callback<Result<UserBO>> callback);


    /**
     * 获取菜单权限
     *
     * @param orgId
     * @param userId
     * @param callback
     */
    @GET("/pvg/role/get_menu_for_pda")
    void commonData( @Query("orgId") String orgId,@Query("userId") String userId,Callback<Result<ArrayList<Authority>>> callback);


    /**
     * 查询未接任务数目
     *
     * @param orgId
     * @param callback
     */
    @GET("/router/warehouse/pda_common_data/pda_common_data")
    void dataCount( @Query("orgId") String orgId,Callback<Result<UserCommon>> callback);

    /**
     * 退出登录
     *
     * @param userId
     * @param warehouseId
     * @param callback
     */
    @GET("/router/warehouse/pda_common_data/pad_logout")
    void logOut( @Query("userId") String userId,
                 @Query("warehouseId") String warehouseId,Callback<Result<String>> callback);
}
