package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.presenter.base.MvpPresenter;
import com.tqmall.utils.callback.ResultCallback;

/**
 * Created by Jay on 16/12/15.
 */

public interface StockInWareDetailPresenter extends MvpPresenter {


    /**
     * 提交数据,上架
     */
    void postData(StockInWareBillBo stockInWareBillBo);

}
