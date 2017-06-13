package com.tqmall.mvp.presenter.impl;


import com.tqmall.global.Result;
import com.tqmall.mvp.activity.StockInWareActivity;
import com.tqmall.mvp.model.api.StockInWareApi;
import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.presenter.StockInWarePresenter;
import com.tqmall.mvp.presenter.base.impl.MvpPresenterImpl;
import com.tqmall.mvp.view.StockInWareView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import com.tqmall.utils.UmengUtils;

import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jay on 16/12/12.
 */

public class StockInWarePresenterImpl extends MvpPresenterImpl<StockInWareView> implements StockInWarePresenter {

    @Inject
    public StockInWarePresenterImpl() {

    }

    @Override
    public void queryInWareBillEntryList(String bnsBillNo , final boolean isAuto) {
        if(mView!=null){
            mView.showProgress("正在加载数据");
            if(!isAuto){
                mView.showRefresh();
            }
        }
        H.getNet(StockInWareApi.class).queryInWareInfoByBnsBillNo(bnsBillNo, new Callback<Result<StockInWareBillBo>>() {
            @Override
            public void success(Result<StockInWareBillBo> stockInWareBillBoResult, Response response) {
                if(mView!=null){
                    mView.hideProgress();
                    if(!isAuto){
                        mView.hideRefresh();
                    }
                    if (stockInWareBillBoResult.success && stockInWareBillBoResult != null) {
                        mView.initAllViews(stockInWareBillBoResult.data);
                    }else {
                        mView.showToastMsg(stockInWareBillBoResult.message, CustomToast.WARN_STATUS);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Logs.e("queryInWareBillEntryList{}", error.toString());
                //TODO
                UmengUtils.reportError(StockInWareActivity.mAct,"StockInWarePresenterImpl.queryInWareBillEntryList()",error.toString());
                if(mView!=null) {
                    mView.hideProgress();
                    if(!isAuto){
                        mView.hideRefresh();
                    }
                    mView.showToastMsg("数据加载异常,请检查网络连接",CustomToast.WARN_STATUS);
                }
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void detachView() {
        super.detachView();
    }

}
