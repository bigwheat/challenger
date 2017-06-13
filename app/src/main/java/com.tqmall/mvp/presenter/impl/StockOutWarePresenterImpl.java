package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.App;
import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockOutWareSearchActivity;
import com.tqmall.mvp.model.Page;
import com.tqmall.mvp.model.api.StockOutApi;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.model.stockout.StockOutWareBillBo;
import com.tqmall.mvp.presenter.StockOutWarePresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockOutWareView;
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
 * Created by Jay on 17/1/16.
 */

public class StockOutWarePresenterImpl extends MvpPresenterImpl<StockOutWareView> implements StockOutWarePresenter {

    @Inject
    StockOutWarePresenterImpl() {

    }

    @Override
    public void queryStockOutInfo(Integer pageNumber, Integer pageSize, final boolean isLoadMore) {
        if (mView != null) {
            mView.showProgress("正在加载");

            if (!isLoadMore) {
                mView.showRefresh();
            } else {
                mView.hideProgress();
            }
            if (!App.isNetworkAvailable(App.mContext)) {
                mView.hideProgress();
                mView.noNetWork();
                mView.showToastMsg("网络中断, 请检查网络连接", CustomToast.WARN_STATUS);
                return;
            }
        }
        H.getNet(StockOutApi.class).queryStockOutInfo(pageSize, pageNumber, new Callback<Result<List<StockOutWareBillBo>>>() {
            @Override
            public void success(Result<List<StockOutWareBillBo>> listResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    if (listResult != null && listResult.success) {
                        List<StockOutWareBillBo> data = listResult.data;
                        mView.initRecycleView(data, isLoadMore);
                    } else {
                        mView.showToastMsg(listResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.toString() != null) {
                    Logs.e("QueryStockOutInfo{}", error.toString());
                    UmengUtils.reportError(StockOutWareSearchActivity.mAct, "StockOutWarePresenterImpl.queryStockOutInfo()", error.toString());
                }
                //为了防止没网的情况下用户手动取消加载进度条出现空指针错误
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }


    @Override
    public void queryStockOutDetailInfo(Integer id) {

        if (mView != null) {
            mView.showProgress("正在加载");
        }
        H.getNet(StockOutApi.class).queryStockOutDetailInfo(id, new Callback<Result<List<StockOutInfoDetail>>>() {
            @Override
            public void success(Result<List<StockOutInfoDetail>> listResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    if (listResult != null && listResult.success && listResult.data != null) {
                        mView.returnStockInfoDetailData(listResult.data);
                    }else {
                        mView.showToastMsg(listResult.message,CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.toString() != null) {
                    Logs.e("queryStockOutDetailInfo{}", error.toString());
                    UmengUtils.reportError(StockOutWareSearchActivity.mAct, "StockOutWarePresenterImpl.queryStockOutDetailInfo()", error.toString());
                }
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });

    }
}
