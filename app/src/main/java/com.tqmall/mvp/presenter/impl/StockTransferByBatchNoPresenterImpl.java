package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockTransferByBatchNoActivity;
import com.tqmall.mvp.model.api.StockTransferApi;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.presenter.StockTransferByBatchNoPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockTransferByBatchNoView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import com.tqmall.utils.UmengUtils;

import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/22.
 */

public class StockTransferByBatchNoPresenterImpl extends MvpPresenterImpl<StockTransferByBatchNoView>
        implements StockTransferByBatchNoPresenter {

    @Inject
    public StockTransferByBatchNoPresenterImpl() {

    }

    /**
     * 以批次码为维度移库,
     * 全部移库完成则关闭页面
     *
     * @param stockTransferWithBatchNoBo
     */
    @Override
    public void transferStockByBatchNo(final StockTransferWithBatchNoBo stockTransferWithBatchNoBo) {
        H.getNet(StockTransferApi.class).transferStockByBatchNo(stockTransferWithBatchNoBo, new Callback<Result<String>>() {
            @Override
            public void success(Result<String> stringResult, Response response) {
                if(mView!=null){
                    if(stringResult.success){
                        mView.isFinishAct(stringResult.success,stringResult.data,stockTransferWithBatchNoBo.batchNo);
                    }else {
                        mView.isFinishAct(stringResult.success,stringResult.message,stockTransferWithBatchNoBo.batchNo);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Logs.e("transferStockByBatchNo{}",error.toString());
                //TODO 异常信息捕获发送,只在debug模式下开启
                UmengUtils.reportError(StockTransferByBatchNoActivity.mAct,
                        "StockTransferByBatchNoPresenterImpl.transferStockByBatchNo()",error.toString());
                if(mView!=null){
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }

    @Override
    public void queryShelvesStockByBatch(String batchNo) {
        mView.showProgress("数据加载中");
        H.getNet(StockTransferApi.class).queryShelvesStockByBatch(batchNo, new Callback<Result<StockTransferWithBatchNoBo>>() {
            @Override
            public void success(Result<StockTransferWithBatchNoBo> stockTransferWithBatchNoBoResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if (stockTransferWithBatchNoBoResult.success && stockTransferWithBatchNoBoResult.data != null) {
                        mView.refreshViews(stockTransferWithBatchNoBoResult.data);
                    } else {
                        mView.showToastMsg(stockTransferWithBatchNoBoResult.message,CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("数据加载异常queryShelvesStockByBatch{},异常信息为:", error.toString());
                //TODO 异常信息捕获发送,只在debug模式下开启
                UmengUtils.reportError(StockTransferByBatchNoActivity.mAct,
                        "StockTransferByBatchNoPresenterImpl.queryShelvesStockByBatch()",error.toString());
                if(mView!=null){
                    mView.showToastMsg("数据加载异常, 请检查网络连接", CustomToast.WARN_STATUS);
                }
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
