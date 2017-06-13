package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockOutWareGoodsDetailActivity;
import com.tqmall.mvp.model.api.StockOutApi;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.presenter.StockOutWareGoodsDetailPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockOutWareGoodsDetailView;
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
 * Created by Jay on 17/1/18.
 */

public class StockOutWareGoodsDetailPresenterImpl extends MvpPresenterImpl<StockOutWareGoodsDetailView>
        implements StockOutWareGoodsDetailPresenter {

    @Inject
    public StockOutWareGoodsDetailPresenterImpl(){

    }


    @Override
    public void postStockOutData(final List<StockOutInfoDetail> stockOutInfoDetailList, final int position) {
        if(mView!=null){
            mView.showProgress("正在提交数据");
        }
        H.getNet(StockOutApi.class).postStockOutData(stockOutInfoDetailList.get(position), new Callback<Result<String>>() {
            @Override
            public void success(Result<String> stringResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if(stringResult.success){
                        mView.showToastMsg("出库成功", CustomToast.SUCCESS_STATUS);
                        mView.dealPosition(stockOutInfoDetailList,position,stockOutInfoDetailList.size());
                    }else {
                        mView.showToastMsg(stringResult.message,CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(mView!=null){
                    if(error.toString()!=null){
                        UmengUtils.reportError(StockOutWareGoodsDetailActivity.mAct,
                                "StockOutWareGoodsDetailPresenterImpl.postStockOutData()",error.toString());
                    }
                    mView.hideProgress();
                    mView.showToastMsg("出库操作异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }


    @Override
    public void queryStockOutDetailInfo(Integer id,final int originDataSize
            , final boolean isRefresh,final int position) {

        if (mView != null) {
            mView.showProgress("正在加载");
        }
        H.getNet(StockOutApi.class).queryStockOutDetailInfo(id, new Callback<Result<List<StockOutInfoDetail>>>() {
            @Override
            public void success(Result<List<StockOutInfoDetail>> listResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    if (listResult != null && listResult.success && listResult.data != null) {
                        mView.initViewPage(listResult.data,position,originDataSize,isRefresh);
                    }else {
                        mView.showToastMsg(listResult.message,CustomToast.WARN_STATUS);
                        mView.initViewPage(null,position,originDataSize,isRefresh);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.toString() != null) {
                    Logs.e("queryStockOutDetailInfo{}", error.toString());
                    UmengUtils.reportError(StockOutWareGoodsDetailActivity.mAct, "StockOutWarePresenterImpl.queryStockOutDetailInfo()", error.toString());
                }
                if (mView != null) {
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });

    }
}
