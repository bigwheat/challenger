package com.tqmall.mvp.presenter.impl;

import android.text.TextUtils;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockTransferByAllocationSnActivity;
import com.tqmall.mvp.model.api.StockTransferApi;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.presenter.StockTransferByAllocationPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockTransferByAllocationView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import com.tqmall.utils.UmengUtils;

import java.util.List;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/23.
 */

public class StockTransferByAllocationPresenterImpl extends MvpPresenterImpl<StockTransferByAllocationView>
        implements StockTransferByAllocationPresenter {


    @Inject
    public StockTransferByAllocationPresenterImpl(){

    }


    @Override
    public void transferStockByAllocationSn(final List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBo, final int position) {
        mView.showProgress("正在移库中");
        H.getNet(StockTransferApi.class).transferStockByAllocationSn(stockTransferWithAllocationSnBo.get(position), new Callback<Result<String>>() {
            @Override
            public void success(Result<String> stringResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if(stringResult.success){
                        mView.showToastMsg(stringResult.data, CustomToast.SUCCESS_STATUS);
                        mView.dealPosition(stockTransferWithAllocationSnBo,position,stockTransferWithAllocationSnBo.size());
                    }else {
                        mView.showToastMsg(stringResult.message,CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("queryInWareBillEntryList{}",error.toString());
                //TODO 异常信息捕获发送,只在debug模式下开启
                UmengUtils.reportError(StockTransferByAllocationSnActivity.mAct,
                        "StockTransferByAllocationPresenterImpl.transferStockByAllocationSn()",error.toString());
                if(mView!=null){
                    mView.showToastMsg("移库失败, 请检查网络连接",CustomToast.WARN_STATUS);
                }
            }
        });
    }

    @Override
    public void queryShelvesStockByAllocationSn(String allocationSn, final int originDataSize
            , final boolean isRefresh,final int position) {
        mView.showProgress("数据加载中");
        H.getNet(StockTransferApi.class).queryShelvesStockByAllocationSn(allocationSn, new Callback<Result<List<StockTransferWithAllocationSnBo>>>() {
            @Override
            public void success(Result<List<StockTransferWithAllocationSnBo>> listResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if (listResult.success) {
                        mView.initViewPage(listResult.data,originDataSize,isRefresh,position);
                    } else {
                        mView.initViewPage(listResult.data,originDataSize,isRefresh,position);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("数据加载异常queryShelvesStockByAllocationSn{},异常信息为:", error.toString());
                //TODO 异常信息捕获发送,只在debug模式下开启
                UmengUtils.reportError(StockTransferByAllocationSnActivity.mAct,
                        "StockTransferByAllocationPresenterImpl.queryShelvesStockByAllocationSn()",error.toString());
                if(mView!=null){
                    mView.showToastMsg("数据加载异常, 请检查网络连接",CustomToast.WARN_STATUS);
                }
            }
        });
    }
}
