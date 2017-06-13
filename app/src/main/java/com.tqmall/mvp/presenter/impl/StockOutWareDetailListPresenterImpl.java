package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockOutWareSearchActivity;
import com.tqmall.mvp.model.api.StockOutApi;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.presenter.StockOutWareDetailListPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockOutWareDetailListView;
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
 * Created by Jay on 17/2/7.
 */

public class StockOutWareDetailListPresenterImpl extends MvpPresenterImpl<StockOutWareDetailListView> implements
        StockOutWareDetailListPresenter {


    @Inject
    public StockOutWareDetailListPresenterImpl(){

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
                    mView.hideRefresh();
                    if (listResult != null && listResult.success) {
                        mView.initRecycleView(listResult.data);
                    }else {
                        mView.showToastMsg(listResult.message, CustomToast.WARN_STATUS);
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
