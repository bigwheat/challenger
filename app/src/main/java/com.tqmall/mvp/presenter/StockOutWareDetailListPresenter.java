package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 17/2/7.
 */

public interface StockOutWareDetailListPresenter extends MvpPresenter {


    /**
     * 查询出库列表详情
     *
     * @param id
     */
    void queryStockOutDetailInfo(Integer id);
}
