package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoEntryBo;
import com.tqmall.mvp.view.base.MvpView;
import java.util.List;

/**
 * Created by Jay on 16/12/22.
 */

public interface StockTransferByBatchNoView extends MvpView {

    void initListView(List<StockTransferWithBatchNoEntryBo> stockTransferWithBatchNoEntryBoList);

    void isFinishAct(Boolean isNeed,String message,String batchNo);

    void refreshViews(StockTransferWithBatchNoBo stockTransferWithBatchNoBo);

}
