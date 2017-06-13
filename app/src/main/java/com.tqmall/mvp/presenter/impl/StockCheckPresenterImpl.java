package com.tqmall.mvp.presenter.impl;

import android.text.TextUtils;

import com.tqmall.global.App;
import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockCheckActivity;
import com.tqmall.mvp.model.Page;
import com.tqmall.mvp.model.api.StockCheckApi;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckInfo;
import com.tqmall.mvp.presenter.StockCheckPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockCheckView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.UmengUtils;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 17/2/7.
 */

public class StockCheckPresenterImpl extends MvpPresenterImpl<StockCheckView> implements StockCheckPresenter {

    @Inject
    public StockCheckPresenterImpl() {

    }


    @Override
    public void queryStockCheckInfo(final Integer pageNumber, Integer pageSize, final boolean isLoadMore) {
        if (mView != null) {
            if (isLoadMore) {
                mView.hideProgress();
            } else {
                mView.showProgress("正在加载");
            }
            if (!App.isNetworkAvailable(App.mContext)) {
                mView.hideProgress();
                mView.noNetWork();
                mView.showToastMsg("网络中断, 请检查网络连接", CustomToast.WARN_STATUS);
                return;
            }
        }
        H.getNet(StockCheckApi.class).queryStockCheckInfo(pageSize, pageNumber, new Callback<Result<Page<StockCheckInfo>>>() {
            @Override
            public void success(Result<Page<StockCheckInfo>> pageResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    if (pageResult.success) {
                        List<StockCheckInfo> stockCheckInfo = pageResult.data.content;
                        mView.initRecycleView(stockCheckInfo, isLoadMore);
                    } else {
                        mView.showToastMsg(pageResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!TextUtils.isEmpty(error.toString())) {
                    UmengUtils.reportError(StockCheckActivity.mAct, "StockCheckPresenterImpl.queryStockCheckInfo()", error.toString());
                }
                if (mView != null) {
                    mView.hideRefresh();
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });

    }

    @Override
    public void queryStockCheckDetailInfo(Integer pageNumber, Integer pageSize, Integer inventoryCheckPlanId, String allocationSn, String batchNo) {
        if (mView != null) {
            mView.showProgress("正在加载");
            if (!App.isNetworkAvailable(App.mContext)) {
                mView.hideProgress();
                mView.noNetWork();
                mView.showToastMsg("网络中断, 请检查网络连接", CustomToast.WARN_STATUS);
                return;
            }
        }
        H.getNet(StockCheckApi.class).queryStockCheckDetailInfo(pageSize, pageNumber, allocationSn, batchNo, inventoryCheckPlanId, new Callback<Result<Page<StockCheckDetailInfo>>>() {
            @Override
            public void success(Result<Page<StockCheckDetailInfo>> pageResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    if (pageResult.success) {
                        List<StockCheckDetailInfo> stockCheckInfo = pageResult.data.content;
                        mView.returnStockInfoDetailData(stockCheckInfo);
                    } else {
                        mView.showToastMsg(pageResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!TextUtils.isEmpty(error.toString())) {
                    UmengUtils.reportError(StockCheckActivity.mAct, "StockCheckPresenterImpl.queryStockCheckDetailInfo()", error.toString());
                }
                if (mView != null) {
                    mView.hideRefresh();
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });

    }
}

