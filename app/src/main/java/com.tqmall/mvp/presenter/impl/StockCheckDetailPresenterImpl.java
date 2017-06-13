package com.tqmall.mvp.presenter.impl;

import android.text.TextUtils;

import com.tqmall.global.App;
import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockCheckActivity;
import com.tqmall.mvp.model.Page;
import com.tqmall.mvp.model.api.StockCheckApi;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.presenter.StockCheckDetailPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockCheckDetailView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.UmengUtils;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 17/2/8.
 */

public class StockCheckDetailPresenterImpl extends MvpPresenterImpl<StockCheckDetailView> implements StockCheckDetailPresenter {

    @Inject
    public StockCheckDetailPresenterImpl() {

    }


    @Override
    public void queryStockCheckDetailInfo(final Integer pageNumber, Integer pageSize, Integer inventoryCheckPlanId, String allocationSn, String batchNo, final boolean isLoadMore) {

        if (mView != null) {
            if (!isLoadMore) {
                mView.showProgress("正在加载");
                mView.showRefresh();
            }
        }
        if (!App.isNetworkAvailable(App.mContext)) {
            mView.hideProgress();
            mView.noNetWork();
            mView.showToastMsg("网络中断, 请检查网络连接", CustomToast.WARN_STATUS);
            return;
        }
        H.getNet(StockCheckApi.class).queryStockCheckDetailInfo(pageSize, pageNumber,allocationSn,batchNo, inventoryCheckPlanId, new Callback<Result<Page<StockCheckDetailInfo>>>() {
            @Override
            public void success(Result<Page<StockCheckDetailInfo>> pageResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    if (pageResult.success) {
                        List<StockCheckDetailInfo> stockCheckInfo = pageResult.data.content;

                        mView.initRecycleView(stockCheckInfo, isLoadMore);
                        if(!TextUtils.isEmpty(pageResult.message) && pageNumber!=null && pageNumber==1){
                            mView.showToastMsg(pageResult.message,CustomToast.WARN_STATUS);
                        }
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

    @Override
    public void postStockCheckData(final List<StockCheckDetailInfo> originData, final int originPos, final List<StockCheckDetailInfo> noChangeData) {
        if(mView!=null){
            mView.showProgress("正在提交数据");
        }
        H.getNet(StockCheckApi.class).postStockCheckData(originData.get(originPos), new Callback<Result<StockCheckDetailInfo>>() {
            @Override
            public void success(Result<StockCheckDetailInfo> stockCheckDetailInfoResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if(stockCheckDetailInfoResult != null && stockCheckDetailInfoResult.success){
                        mView.initRecycleViewAfterPostData(originData,originPos,stockCheckDetailInfoResult.data,noChangeData,true);
                        mView.showToastMsg(stockCheckDetailInfoResult.message,CustomToast.SUCCESS_STATUS);
                    }else {
                        mView.showToastMsg(stockCheckDetailInfoResult.message,CustomToast.WARN_STATUS);
                        mView.initRecycleViewAfterPostData(originData,originPos,null,noChangeData,false);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!TextUtils.isEmpty(error.toString())) {
                    UmengUtils.reportError(StockCheckActivity.mAct, "StockCheckPresenterImpl.postStockCheckData()", error.toString());
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
