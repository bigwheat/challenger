package com.tqmall.mvp.presenter.impl;


import android.content.DialogInterface;
import android.view.View;

import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.global.Result;
import com.tqmall.mvp.activity.HomeActivity;
import com.tqmall.mvp.activity.LoginActivity;
import com.tqmall.mvp.model.user.Authority;
import com.tqmall.mvp.model.user.UserCommon;
import com.tqmall.mvp.model.api.UserInfoApi;
import com.tqmall.mvp.model.cache.LoginCache;
import com.tqmall.mvp.model.cache.UserCache;
import com.tqmall.mvp.presenter.HomePresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.HomeView;
import com.tqmall.mvp.widget.CustomDialogForNormal;
import com.tqmall.utils.H;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/9.
 */

public class HomePresenterImpl extends MvpPresenterImpl<HomeView> implements HomePresenter {


    public static int count;

    @Inject
    public HomePresenterImpl() {

    }

    /**
     * 加载用户信息
     */
    @Override
    public void loadUserInfo() {

//        mView.showProgress("加载中...");
//        mView.showMsg("正在加载用户信息");
        if (!App.isNetworkAvailable(App.mContext)&&mView!=null) {
            mView.showMsg("请检查网络连接");
        }

        if (count > 3) {
            if (mView!=null) {
                mView.showMsg("加载用户信息失败,正在退出 , 请稍候");
            }
            LoginCache.getIns().logout();
            IntentUtils.launch(HomeActivity.mAct, LoginActivity.class);
        } else {
            count++;
            UserCache.getIns().loadUserInfo(new Runnable() {
                @Override
                public void run() {
//                    mView.hideProgress();
                    if (UserCache.getIns().getUserId() == null) {
                        loadUserInfo();
                    } else {
//                        Logs.d("userId=", String.valueOf(UserCache.getIns().getUserId()));
                        if (null == UserCache.getIns().getWareHouseId()) {
//                            CustomDialog.alert(HomeActivity.mAct, "没有默认仓库，你可以：\n1.在PC端分配默认仓库 \n2.点击“确定”切换账号 \n3.重启软件后再试"
//                                    , "提示", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if(mView!=null){
//                                                mView.showProgress("正在退出 , 请稍候");
//                                                mView.hideProgress();
//                                            }
//                                            LoginCache.getIns().logout();
//                                            IntentUtils.launch(HomeActivity.mAct, LoginActivity.class);
//                                        }
//                                    }, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    }
//                            );
                            View item=View.inflate(HomeActivity.mAct, R.layout.dialog_normal_layout,null);
                            new CustomDialogForNormal.Builder(HomeActivity.mAct)
                                    .setTitle("提示")
                                    .setMessage("没有默认仓库，你可以：\n1.在PC端分配默认仓库 \n2.点击“确定”切换账号 \n3.重启软件后再试")
                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(mView!=null){
                                                mView.showProgress("正在退出 , 请稍候");
                                                mView.hideProgress();
                                            }
                                            LoginCache.getIns().logout();
                                            IntentUtils.launch(HomeActivity.mAct, LoginActivity.class);
                                            dialog.dismiss();
                                        }
                                    })
                                    .setContentView(item)
                                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        } else {
                            if (!App.isNetworkAvailable(App.mContext)&&mView!=null) {
                                mView.showMsg("请检查网络连接");
//                                mView.showMsg("成功加载用户信息");
                            }
                        }
//                        getCommonData();
//                        getCommonDataCount();
                    }
                }
            });
        }
    }

    /**
     * 初始化用户权限信息
     *
     * @param list
     */
    @Override
    public void initPermission(List<String> list) {
        mView.initPermissionInActivity(list);
    }


    /**
     * 获取用户菜单权限
     */
    private void getCommonData() {
        mView.showProgress("正在获取权限信息");

        H.getNet(UserInfoApi.class).commonData(UserCache.getIns().getUserInfo().orgId.toString(), UserCache.getIns().getUserId().toString(), new Callback<Result<ArrayList<Authority>>>() {
            @Override
            public void success(Result<ArrayList<Authority>> result, Response response) {
                mView.hideProgress();
                List<String> userRole = new ArrayList<>();
                if (result == null) {
                    mView.showMsg("服务器未返回数据 , 请稍后再试 ");
                    return;
                }
                if (result.success) {
                    if(mView!=null){
                        mView.showMsg("已成功载入权限信息");
                    }
                    ArrayList<Authority> AuthoList = result.data;
                    if (AuthoList != null) {
                        for (int i = 0; i < AuthoList.size(); i++) {
                            Authority TmpObj = AuthoList.get(i);
                            if (TmpObj != null) {
                                if (TmpObj.groupName.indexOf("PDA") >= 0) {
                                    ArrayList<Authority> ChildList = TmpObj.menuVoList;
                                    if (ChildList != null) {
                                        for (int j = 0; j < ChildList.size(); j++) {
                                            Authority TmpChild = ChildList.get(j);
                                            if (TmpChild != null) {
                                                if (TmpChild.groupName != null) {
                                                    if (TmpChild.groupName.indexOf("验货") >= 0)
                                                        userRole.add("pda_stock_checkin");
                                                    else if (TmpChild.groupName.indexOf("上架") >= 0)
                                                        userRole.add("pda_stock_shelves");
                                                    else if (TmpChild.groupName.indexOf("查询") >= 0)
                                                        userRole.add("pda_stock_query");
                                                    else if (TmpChild.groupName.indexOf("盘点") >= 0)
                                                        userRole.add("pda_stock_count");
                                                    else if (TmpChild.groupName.indexOf("复核出库") >= 0)
                                                        userRole.add("pda_stock_checkout");
                                                    else if (TmpChild.groupName.indexOf("备货") >= 0)
                                                        userRole.add("pda_stock_prepare");
                                                }
                                            }
                                        }
                                        initPermission(userRole);
                                    }
                                }
                            }
                        }
                    }
                } else {
//                    Logs.e("获取权限信息失败:{}", result.message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.d("权限获取出错：", error.toString());
                if (null != error.getResponse() &&
                        401 == error.getResponse().getStatus()) {
                    IntentUtils.launch(HomeActivity.mAct, LoginActivity.class);
                } else {
                    loadUserInfo();
                }
                mView.hideProgress();
            }
        });
    }


    private void getCommonDataCount() {

        mView.hideProgress();
        mView.showProgress("正在加载数据");
        H.getNet(UserInfoApi.class).dataCount(UserCache.getIns().getUserInfo().orgId.toString(), new Callback<Result<UserCommon>>() {
            @Override
            public void success(Result<UserCommon> result, Response response) {
                mView.hideProgress();
                if (result == null) {
                    mView.showMsg("服务器未返回数据，请稍后重试");
                    return;
                }
                if (result.success) {
                    UserCommon data = result.data;
                    if (null != data.countSum) {
                        if (null != data.countSum.prepare && data.countSum.prepare > 0) {
//                            tvPrepareNum.setVisibility(View.VISIBLE);
//                            tvPrepareNum.setText(data.countSum.prepare + "");
                        } else {
//                            tvPrepareNum.setVisibility(View.GONE);
                        }
                        if (null != data.countSum.checkOut && data.countSum.checkOut > 0) {
//                            tvCheckoutNum.setVisibility(View.VISIBLE);
//                            tvCheckoutNum.setText(data.countSum.checkOut + "");
                        } else {
//                            tvCheckoutNum.setVisibility(View.GONE);
                        }
                        if (null != data.countSum.count && data.countSum.count > 0) {
//                            tvCountNum.setVisibility(View.VISIBLE);
//                            tvCountNum.setText(data.countSum.count + "");
                        } else {
//                            tvCountNum.setVisibility(View.GONE);
                        }
                        if (null != data.countSum.checkIn && data.countSum.checkIn > 0) {
//                            tvCheckInNum.setVisibility(View.VISIBLE);
//                            tvCheckInNum.setText(data.countSum.checkIn + "");
                        } else {
//                            tvCheckInNum.setVisibility(View.GONE);
                        }
                        if (null != data.countSum.goodsShelves && data.countSum.goodsShelves > 0) {
//                            tvPutOnShelves.setVisibility(View.VISIBLE);

//                            tvPutOnShelves.setText(data.countSum.goodsShelves + "");
                        } else {
//                            tvPutOnShelves.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Logs.e("加载任务数据失败:{}", result.message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mView.hideProgress();
                Logs.d("权限获取出错：", error.toString());
                if (null != error.getResponse() &&
                        401 == error.getResponse().getStatus()) {
                    IntentUtils.launchClearTop(HomeActivity.mAct, LoginActivity.class, LoginActivity.isFront);
                } else {
                    loadUserInfo();
                }
            }
        });

    }


    @Override
    public void onCreate() {
        super.onCreate();
        loadUserInfo();
    }
}
