package com.tqmall.mvp.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pgyersdk.update.PgyUpdateManager;
import com.tqmall.R;
import com.tqmall.di.component.ActivityComponent;
import com.tqmall.di.component.DaggerActivityComponent;
import com.tqmall.di.module.ActivityModule;
import com.tqmall.global.App;
import com.tqmall.mvp.presenter.base.MvpPresenter;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;


public abstract class BaseActivity<P extends MvpPresenter> extends AppCompatActivity{

    protected ActivityComponent mActivityComponent;

    protected P mPresenter;

    protected static boolean front = false;

    public static BaseActivity mAct;

    public boolean running = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityComponent();
        setContentView(getLayoutRs());
        mAct = this;
        ButterKnife.bind(this);
        MobclickAgent.setDebugMode(true);
        initToolBar();
        presenterInject();
        App.getIns().addActivity(this);
        initData();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        front=true;
        running=true;
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    /**
     * 在关闭activity层时候销毁P层持有的view对象
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        PgyUpdateManager.unregister();
    }

    /**
     * 初始化ActivityComponent
     */
    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public Boolean running() {
        return running;
    }

    protected abstract int getLayoutRs();

    protected abstract void initData();

    protected abstract void presenterInject();

}
