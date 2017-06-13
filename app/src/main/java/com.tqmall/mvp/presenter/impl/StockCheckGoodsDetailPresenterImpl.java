package com.tqmall.mvp.presenter.impl;

import android.text.TextUtils;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockCheckActivity;
import com.tqmall.mvp.model.api.StockCheckApi;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.presenter.StockCheckGoodsDetailPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockCheckGoodsDetailView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.UmengUtils;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 17/2/9.
 */

public class StockCheckGoodsDetailPresenterImpl extends MvpPresenterImpl<StockCheckGoodsDetailView> implements StockCheckGoodsDetailPresenter {

    @Inject
    public StockCheckGoodsDetailPresenterImpl(){

    }

    @Override
    public void postStockCheckData(StockCheckDetailInfo stockCheckDetailInfo) {
        if(mView!=null){
            mView.showProgress("正在提交数据");
        }

        H.getNet(StockCheckApi.class).postStockCheckData(stockCheckDetailInfo, new Callback<Result<StockCheckDetailInfo>>() {
            @Override
            public void success(Result<StockCheckDetailInfo> stockCheckDetailInfoResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if(stockCheckDetailInfoResult != null && stockCheckDetailInfoResult.success){
                        mView.initView(stockCheckDetailInfoResult.data);
                        mView.showToastMsg(stockCheckDetailInfoResult.message,CustomToast.SUCCESS_STATUS);
                    }else {
                        mView.showToastMsg(stockCheckDetailInfoResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!TextUtils.isEmpty(error.toString())) {
                    UmengUtils.reportError(StockCheckActivity.mAct, "StockCheckGoodsDetailPresenterImpl.postStockCheckData()", error.toString());
                }
                if (mView != null) {
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }
}
