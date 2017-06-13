package com.tqmall.di.component;

import android.app.Activity;
import android.content.Context;
import com.tqmall.di.module.ActivityModule;
import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerActivity;
import com.tqmall.mvp.activity.HomeActivity;
import com.tqmall.mvp.activity.SearchActivity;
import com.tqmall.mvp.activity.SettingsActivity;
import com.tqmall.mvp.activity.StockCheckActivity;
import com.tqmall.mvp.activity.StockCheckDetailActivity;
import com.tqmall.mvp.activity.StockCheckGoodsDetailActivity;
import com.tqmall.mvp.activity.StockInWareActivity;
import com.tqmall.mvp.activity.LoginActivity;
import com.tqmall.mvp.activity.StockInWareDetailActivity;
import com.tqmall.mvp.activity.StockInWareSearchActivity;
import com.tqmall.mvp.activity.StockOutWareDetailListActivity;
import com.tqmall.mvp.activity.StockOutWareGoodsDetailActivity;
import com.tqmall.mvp.activity.StockOutWareSearchActivity;
import com.tqmall.mvp.activity.StockTransferByAllocationSnActivity;
import com.tqmall.mvp.activity.StockTransferChooseActivity;
import com.tqmall.mvp.activity.StockTransferSearchActivity;
import com.tqmall.mvp.activity.StockTransferByBatchNoActivity;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(LoginActivity loginActivity);

    void inject(HomeActivity homeActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(StockInWareActivity inStorageActivity);

    void inject(StockInWareDetailActivity stockInWareDetailActivity);

    void inject(StockTransferSearchActivity stockTransferSearchActivity);

    void inject(StockTransferByBatchNoActivity stockTransferByBatchNoActivity);

    void inject(StockTransferByAllocationSnActivity stockTransferByAllocationSnActivity);

    void inject(SearchActivity searchActivity);

    void inject(StockInWareSearchActivity stockInWareSearchActivity);

    void inject(StockTransferChooseActivity stockTransferChooseActivity);

    void inject(StockOutWareSearchActivity stockOutWareActivity);

    void inject(StockOutWareGoodsDetailActivity stockOutWareGoodsDetailActivity);

    void inject(StockOutWareDetailListActivity stockOutWareDetailListActivity);

    void inject(StockCheckActivity stockCheckActivity);

    void inject(StockCheckDetailActivity stockCheckDetailActivity);

    void inject(StockCheckGoodsDetailActivity stockCheckGoodsDetailActivity);
}
