package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 17/2/9.
 */

public interface StockCheckGoodsDetailPresenter extends MvpPresenter {


    /**
     * 提交盘点数据
     *
     * @param stockCheckDetailInfo
     */
    void postStockCheckData(StockCheckDetailInfo stockCheckDetailInfo);


}
