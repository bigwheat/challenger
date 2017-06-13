package com.tqmall.mvp.presenter;

import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 17/2/7.
 */

public interface StockCheckPresenter extends MvpPresenter {

    /**
     * 查询盘点列表
     *
     * @param pageNumber
     * @param pageSize
     * @param isLoadMore
     */
    void queryStockCheckInfo(Integer pageNumber, Integer pageSize, boolean isLoadMore);


    /**
     * 查询盘点列表商品详情
     *
     * @param pageNumber
     * @param pageSize
     * @param inventoryCheckPlanId
     * @param allocationSn
     * @param batchNo
     */
    void queryStockCheckDetailInfo(Integer pageNumber, Integer pageSize,Integer inventoryCheckPlanId, String allocationSn,String batchNo);

}
