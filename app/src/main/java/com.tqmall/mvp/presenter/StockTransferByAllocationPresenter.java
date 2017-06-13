package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.presenter.base.MvpPresenter;

import java.util.List;

/**
 * Created by Jay on 16/12/23.
 */

public interface StockTransferByAllocationPresenter extends MvpPresenter {


    /**
     * 以商品为维度进行移库
     *
     * @param data
     * @param position
     */
    void transferStockByAllocationSn(List<StockTransferWithAllocationSnBo> data,int position);

    /**
     * 根据商品库位码查询移库信息
     *
     * @param allocationSn
     * @return
     */
    void  queryShelvesStockByAllocationSn(String allocationSn, int originDataSize, boolean isRefresh,int position);

}
