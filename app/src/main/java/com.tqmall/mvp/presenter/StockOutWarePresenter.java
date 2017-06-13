package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 17/1/16.
 */

public interface StockOutWarePresenter extends MvpPresenter {


    /**
     * 查询出库列表
     *
     * @param pageNumber
     * @param pageSize
     * @param isLoadMore
     */
    void queryStockOutInfo(Integer pageNumber, Integer pageSize, boolean isLoadMore);

    /**
     * 查询出库列表详情
     *
     * @param id
     */
    void queryStockOutDetailInfo(Integer id);

}
