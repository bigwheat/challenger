package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 16/12/23.
 */

public interface StockTransferByAllocationView extends MvpView {

    void initViewPage(List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList,int originDataSize, boolean isRefresh,int position);

    void dealPosition(List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBo, int position,int size);

}
