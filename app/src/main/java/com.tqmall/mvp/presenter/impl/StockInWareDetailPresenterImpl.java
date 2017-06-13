package com.tqmall.mvp.presenter.impl;

import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockInWareDetailActivity;
import com.tqmall.mvp.model.api.StockInWareApi;
import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.StockInWareDetailPresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockInWareDetailView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import com.tqmall.utils.UmengUtils;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/15.
 */

public class StockInWareDetailPresenterImpl extends MvpPresenterImpl<StockInWareDetailView> implements StockInWareDetailPresenter {


    @Inject
    public StockInWareDetailPresenterImpl() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * 进行入库操作 添加对入库请求的异常发送
     * 1.如果是部分入库则入库完成后刷新页面
     * 2.如果是全部已入库则不刷新页面
     *
     * @param stockInWareBillBo
     */
    @Override
    public void postData(final StockInWareBillBo stockInWareBillBo) {
        mView.showProgress("正在加载数据");
        H.getNet(StockInWareApi.class).goodsOnShelves(stockInWareBillBo, new Callback<Result<StockInWareInfo>>() {
            @Override
            public void success(Result<StockInWareInfo> stockInfoResult, Response response) {
                if (mView != null) {
                    mView.hideProgress();
                    if (stockInfoResult.success) {
                        mView.refreshViews(stockInfoResult.data, stockInfoResult.code, stockInfoResult.message);
                    } else {
                        mView.showToastMsg(stockInfoResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("数据加载异常postData{},异常信息为:", error.toString());
                //TODO 异常信息捕获发送
                    UmengUtils.reportError(StockInWareDetailActivity.mAct,"StockInWareDetailPresenterImpl.postData()",error.toString());
                if (mView != null) {
                    mView.hideProgress();
                    mView.showToastMsg("入库失败, 请检查网络连接",CustomToast.WARN_STATUS);
                }
            }
        });

    }
}
