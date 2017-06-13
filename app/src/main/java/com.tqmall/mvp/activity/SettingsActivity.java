package com.tqmall.mvp.activity;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tqmall.BuildConfig;
import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.cache.UserCache;
import com.tqmall.mvp.model.user.UserBO;
import com.tqmall.mvp.model.user.UserCenterBo;
import com.tqmall.mvp.presenter.impl.SettingsPresenterImpl;
import com.tqmall.mvp.view.SettingsView;
import com.tqmall.mvp.widget.CustomDialogForNormal;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 16/12/8.
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener, SettingsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.logout)
    TextView logout;

    @BindView(R.id.content)
    LinearLayout coordinatorLayout;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;

    @BindView(R.id.tvSection)
    TextView tvSection;

    @BindView(R.id.tvJobNumber)
    TextView tvJobNumber;

    @BindView(R.id.tvMobile)
    TextView tvMobile;

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @Inject
    SettingsPresenterImpl settingsPresenter;

    @Override
    protected int getLayoutRs() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_settings;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
//            Alert.alert(mAct, "退出后将不会保存您当前的任务进度,确认退出登录么?", "提示",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            settingsPresenter.logout();
//                        }
//                    }, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//            LayoutInflater inflater=getLayoutInflater();
//            View view=inflater.inflate(R.layout.dialog_normal_layout,null);

            new CustomDialogForNormal.Builder(this)
                    .setMessage("请确认是否退出 ?")
                    .setTitle("提示")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            settingsPresenter.logout();
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
    }

    @Override
    protected void initData() {
        logout.setOnClickListener(this);
        UserBO userBO = UserCache.getIns().getUserInfo();
        if (userBO != null) {
            if(App.isDebug()){
                tvVersion.setText("V" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
            }else {
                tvVersion.setText("D" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
            }
        }
        settingsPresenter.initData();
    }

    @Override
    public void initData(UserCenterBo userCenterBo) {
        if(userCenterBo!=null){
            tvUserName.setText(userCenterBo.userName);
            tvCompanyName.setText(userCenterBo.orgName);
            tvJobNumber.setText(userCenterBo.staffNo);
            tvMobile.setText(userCenterBo.mobilePhone);
        }
    }

    @OnClick({R.id.back})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = settingsPresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void showMsg(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToastMsg(String message, int status) {

    }
}
