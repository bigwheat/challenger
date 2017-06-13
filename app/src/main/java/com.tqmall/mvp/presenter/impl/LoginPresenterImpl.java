package com.tqmall.mvp.presenter.impl;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.mvp.activity.HomeActivity;
import com.tqmall.mvp.activity.LoginActivity;
import com.tqmall.mvp.model.cache.LoginCache;
import com.tqmall.mvp.model.cache.UserCache;
import com.tqmall.mvp.presenter.LoginPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.LoginView;
import com.tqmall.mvp.widget.CustomDialogForNormal;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.callback.ResultCallback;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

/**
 * Created by Jay on 16/11/29.
 */


public class LoginPresenterImpl extends MvpPresenterImpl<LoginView> implements LoginPresenter {


    @Inject
    public LoginPresenterImpl() {

    }

    @Override
    public void login(String userName, String userPassword, boolean remember, final ResultCallback callback) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userPassword)) {
            if(mView!=null){
                mView.showProgress("登录中...");
            }
            LoginCache.getIns().login(userName, userPassword, remember, new ResultCallback() {
                @Override
                public void success() {
                    if(mView!=null){
                        mView.hideProgress();
                        mView.showMsg("  登录成功");
                    }
                    MobclickAgent.onProfileSignIn(String.valueOf(UserCache.getIns().getUserId()));
                    IntentUtils.launch(LoginActivity.mAct, HomeActivity.class);
                    LoginActivity.mAct.finish();
                }

                @Override
                public void error(final String message) {
                    if(mView!=null){
                        mView.hideProgress();
                    }
                    App.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            View viewItem=View.inflate(LoginActivity.mAct,R.layout.dialog_login_error_layout,null);
                            new CustomDialogForNormal.Builder(LoginActivity.mAct)
                                    .setContentView(viewItem)
                                    .setMessage(message)
                                    .setTitle("提示")
                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if(mView!=null){
                                                mView.hideProgress();
                                            }
                                        }
                                    })
                                    .create()
                                    .show();

                        }
                    });

                }
            });
        }
    }
}
