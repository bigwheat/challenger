package com.tqmall.mvp.model.stockTransfer;

import com.tqmall.global.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jay on 16/12/23.
 */

public class StockTransferWithAllocationAgent extends BaseBean implements Serializable {

    public List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList;
}
