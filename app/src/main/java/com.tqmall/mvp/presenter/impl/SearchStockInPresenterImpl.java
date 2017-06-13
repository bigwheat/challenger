package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.Result;
import com.tqmall.mvp.model.api.StockInWareApi;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.SearchStockInPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.SearchStockInView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 17/1/4.
 */

public class SearchStockInPresenterImpl extends MvpPresenterImpl<SearchStockInView> implements SearchStockInPresenter {

    @Inject
    public SearchStockInPresenterImpl() {

    }

    @Override
    public void initStockInWareData(String batchNo) {
        mView.showProgress("正在加载数据");
        H.getNet(StockInWareApi.class).queryStockInWareInfoByBatchNo(batchNo, new Callback<Result<StockInWareInfo>>() {
            @Override
            public void success(Result<StockInWareInfo> stockInWareInfoResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();

//                if (stockInWareInfoResult.data != null) {
//                    Logs.d("initStockInWareData{}", stockInWareInfoResult.data.toString());
//                }
                    StockInWareInfo stockInWareInfo = stockInWareInfoResult.data;
                    if (stockInWareInfoResult.success) {
                        mView.returnData(stockInWareInfo);
                    } else if (mView != null) {
                        mView.returnData(null);
                        mView.showToastMsg(stockInWareInfoResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("数据加载异常initStockInWareData{},异常信息为:", error.toString());

                if (mView != null) {
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }
}
