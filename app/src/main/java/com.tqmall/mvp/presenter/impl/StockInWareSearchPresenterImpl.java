package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.App;
import com.tqmall.global.Result;
import com.tqmall.mvp.model.api.StockInWareApi;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.StockInWareSearchPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockInWareSearchView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/19.
 */

public class StockInWareSearchPresenterImpl extends MvpPresenterImpl<StockInWareSearchView> implements StockInWareSearchPresenter {


    @Inject
    public StockInWareSearchPresenterImpl() {

    }

    @Override
    public void initRecycleViewData(Integer pageSize, final Integer pageNumber, final boolean isLoadMore) {
        if (mView != null) {
            mView.showProgress("数据加载中");
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

        H.getNet(StockInWareApi.class).queryInWareGoodsInfoIndex(pageSize, pageNumber, new Callback<Result<List<StockInWareInfo>>>() {
            @Override
            public void success(Result<List<StockInWareInfo>> listResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    if (listResult.success) {
                        List<StockInWareInfo> stockInWareInfoList = listResult.data;
                        if (pageNumber == 1 && (stockInWareInfoList == null || stockInWareInfoList.size() == 0)) {
                            mView.noData();
                            return;
                        }
                        mView.initRecycle(stockInWareInfoList, isLoadMore);
                    } else {
                        mView.showToastMsg(listResult.message, CustomToast.WARN_STATUS);
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("queryInWareGoodsInfoIndex{}", error.toString());
                //为了防止没网的情况下用户手动取消加载进度条出现空指针错误
                if (mView != null) {
                    mView.hideProgress();
                    mView.hideRefresh();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }

    /**
     * 根据批次码请求数据
     *
     * @param batchNo
     */
    @Override
    public void initStockInWareData(String batchNo) {
        mView.showProgress("数据加载中");
        if (!App.isNetworkAvailable(App.mContext) && mView != null) {
            mView.hideProgress();
            mView.showToastMsg("网络中断,请检查网络连接", CustomToast.WARN_STATUS);
            return;
        }
        H.getNet(StockInWareApi.class).queryStockInWareInfoByBatchNo(batchNo, new Callback<Result<StockInWareInfo>>() {
            @Override
            public void success(Result<StockInWareInfo> stockInWareInfoResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    StockInWareInfo stockInWareInfo = stockInWareInfoResult.data;
                    if (stockInWareInfoResult.success) {
                        mView.returnData(stockInWareInfo);
                    } else {
                        mView.showToastMsg(stockInWareInfoResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("根据批次码请求数据[initStockInWareData()],异常信息为:", error.toString() + "--->" + error.getKind() + "--");
                if (mView != null) {
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }
}
