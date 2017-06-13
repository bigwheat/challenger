package com.tqmall.mvp.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.tqmall.BuildConfig;
import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.cache.LoginCache;
import com.tqmall.mvp.presenter.impl.LoginPresenterImpl;
import com.tqmall.mvp.view.LoginView;
import com.tqmall.mvp.widget.CustomDialogForNormal;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.Logs;
import com.tqmall.utils.callback.ResultCallback;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * Created by Jay on 16/11/28.
 */

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {


    @BindView(R.id.login)
    TextView login;

    @BindView(R.id.userName)
    EditText userName;

    @BindView(R.id.userPwd)
    EditText userPwd;

    @BindView(R.id.cbRemember)
    CheckBox cbRemember;

    @BindView(R.id.content)
    ConstraintLayout constraintLayout;

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @Inject
    LoginPresenterImpl mainPresenter;

    private ProgressDialog progressDialog;

    private ResultCallback callback;

    public static boolean isFront = false;

    //是否需要自动更新
    public boolean isUpdate = false;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_login;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
//        linearLayout.setAlpha(0.3F);
        setListener();
        isNeedUpdate();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:
                if (isUpdate) {
                    isNeedUpdate();
                } else {
                    String userNameStr=userName.getText().toString().trim();
                    String userPwdStr=userPwd.getText().toString().trim();
                    if(TextUtils.isEmpty(userNameStr)){
                        showToastMsg("请输入账号",CustomToast.WARN_STATUS);
                        return;
                    }
                    if(TextUtils.isEmpty(userPwdStr)){
                        showToastMsg("请输入密码",CustomToast.WARN_STATUS);
                        return;
                    }
                    mainPresenter.login(userName.getText().toString().trim(),
                            userPwd.getText().toString().trim(),
                            cbRemember.isChecked(), callback);
                }
                break;
            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.KEYCODE_BACK == keyCode && event.getRepeatCount() == 0) {
            App.getIns().onTerminate();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 检查更新
     */
    private void isNeedUpdate() {

        PgyUpdateManager.register(LoginActivity.this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        isUpdate = true;
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        Logs.d("AppBean:{}", appBean.toString());
//                        new AlertDialog.Builder(LoginActivity.this)
//                                .setTitle("更新")
//                                .setMessage("当前有新版本发布,请更新")
//                                .setNegativeButton(
//                                        "确定",
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                startDownloadTask(
//                                                        LoginActivity.this,
//                                                        appBean.getDownloadURL());
//                                            }
//                                        }).show();


                        new CustomDialogForNormal.Builder(mAct)
                                .setMessage("当前有新版本发布,请更新")
                                .setTitle("更新")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDownloadTask(
                                                LoginActivity.this,
                                                appBean.getDownloadURL());
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        isUpdate = false;
                        judgeLogout();
                    }
                });

    }

    /**
     * 注入依赖,开启P层与Activity的关联
     */
    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = mainPresenter;
        mPresenter.attachView(this);
    }

    private void setListener() {
        login.setOnClickListener(this);
        tvVersion.setText("V" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
    }

    /**
     * 判断缓存是否存在
     */
    private void judgeLogout() {
        try {
            String userNameStr = LoginCache.getIns().getLoginInfo().get(Constant.USER_NAME);
            String userPasswordStr = LoginCache.getIns().getLoginInfo().get(Constant.USER_PASSWORD);
            if (!TextUtils.isEmpty(userNameStr)
                    && !TextUtils.isEmpty(userPasswordStr)) {
                userName.setText(userNameStr);
                mainPresenter.login(userNameStr, userPasswordStr, cbRemember.isChecked(), callback);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showProgress(String message) {
        progressDialog = Alert.progress(mAct, message, true);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMsg(String message) {
        progressDialog.dismiss();
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showToastMsg(String message, int status) {
        hideProgress();
        CustomToast.showToast(mAct, message, status);
    }

    public static void launch() {
        if (!isFront) {
            Intent intent = new Intent(App.getIns(), LoginActivity.class);
            if (null != BaseActivity.mAct && BaseActivity.mAct.running()) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BaseActivity.mAct.startActivity(intent);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getIns().startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }
}
