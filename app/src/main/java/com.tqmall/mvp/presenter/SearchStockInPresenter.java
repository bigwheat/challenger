package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 16/12/19.
 */

public interface SearchStockInPresenter extends MvpPresenter {

    /**
     * 根据批次码获取数据
     */
    void initStockInWareData(String batchNo);

}
