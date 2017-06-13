package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockTransferSearchActivity;
import com.tqmall.mvp.model.api.StockTransferApi;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.presenter.StockTransferSearchPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockTransferSearchView;
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
 * Created by Jay on 17/1/14.
 */

public class StockTransferChoosePresenterImpl extends MvpPresenterImpl<StockTransferSearchView> implements StockTransferSearchPresenter {

    @Inject
    public StockTransferChoosePresenterImpl() {

    }

    @Override
    public void queryShelvesStockByBatch(String batchNo) {
        if(mView!=null){
            mView.showProgress("数据加载中");
        }
        H.getNet(StockTransferApi.class).queryShelvesStockByBatch(batchNo, new Callback<Result<StockTransferWithBatchNoBo>>() {
            @Override
            public void success(Result<StockTransferWithBatchNoBo> stockTransferWithBatchNoBoResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if (stockTransferWithBatchNoBoResult.success && stockTransferWithBatchNoBoResult.data != null) {
                        mView.returnData(stockTransferWithBatchNoBoResult.data);
                    } else {
                        mView.returnData(null);
                        mView.showToastMsg(stockTransferWithBatchNoBoResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("数据加载异常queryShelvesStockByBatch{},异常信息为:", error.toString());
                UmengUtils.reportError(StockTransferSearchActivity.mAct,
                        "StockTransferSearchPresenterImpl.queryShelvesStockByBatch()",error.toString());
                if(mView!=null){
                    mView.hideProgress();
                    mView.showToastMsg("数据加载异常, 请检查网络连接",CustomToast.WARN_STATUS);
                }
            }
        });
    }

    @Override
    public void queryShelvesStockByAllocationSn(String allocationSn) {
        if(mView!=null){
            mView.showProgress("数据加载中");
        }
        H.getNet(StockTransferApi.class).queryShelvesStockByAllocationSn(allocationSn, new Callback<Result<List<StockTransferWithAllocationSnBo>>>() {
            @Override
            public void success(Result<List<StockTransferWithAllocationSnBo>> listResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if (listResult.success) {
                        mView.returnDataByQueryInfo(listResult.data);
                    } else {
                        mView.returnDataByQueryInfo(null);
                        mView.showToastMsg(listResult.message,CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("数据加载异常queryShelvesStockByAllocationSn{},异常信息为:", error.toString());
                UmengUtils.reportError(StockTransferSearchActivity.mAct,
                        "StockTransferSearchPresenterImpl.queryShelvesStockByAllocationSn()",error.toString());
                if(mView!=null){
                    mView.showToastMsg("数据加载异常, 请检查网络连接",CustomToast.WARN_STATUS);
                }
            }
        });
    }
}
