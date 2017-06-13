package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 16/12/22.
 */

public interface StockTransferByBatchNoPresenter extends MvpPresenter {

    /**
     * 以批次码为维度移库
     *
     * @param stockTransferWithBatchNoBo
     */
    void transferStockByBatchNo(StockTransferWithBatchNoBo stockTransferWithBatchNoBo);

    /**
     * 根据商品批次码查询商品移库信息
     *
     * @param batchNo
     */
    void queryShelvesStockByBatch(String batchNo);
}
