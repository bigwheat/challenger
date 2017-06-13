package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 16/12/22.
 */

public interface StockTransferSearchView extends MvpView {

    void returnData(StockTransferWithBatchNoBo stockTransferWithBatchNoBo);

    void returnDataByQueryInfo(List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList);
}
