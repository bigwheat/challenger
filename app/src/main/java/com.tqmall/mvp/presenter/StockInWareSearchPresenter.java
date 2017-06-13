package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 16/12/19.
 */

public interface StockInWareSearchPresenter extends MvpPresenter {

    void initRecycleViewData(Integer pageSize,Integer pageNumber,boolean isLoadMore);

    /**
     * 初始化页面数据
     */
    void initStockInWareData(String batchNo);

}
