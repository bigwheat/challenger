package com.tqmall.mvp.presenter;

import com.tqmall.global.Result;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.presenter.base.MvpPresenter;

import java.util.List;

/**
 * Created by Jay on 16/12/20.
 */

public interface StockTransferSearchPresenter extends MvpPresenter {

    /**
     * 根据商品批次码查询商品移库信息
     *
     * @param batchNo
     */
    void queryShelvesStockByBatch(String batchNo);


    /**
     * 根据商品库位码查询移库信息
     *
     * @param allocationSn
     * @return
     */
     void  queryShelvesStockByAllocationSn(String allocationSn);

}
