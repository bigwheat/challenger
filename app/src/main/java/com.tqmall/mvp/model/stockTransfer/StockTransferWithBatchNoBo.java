package com.tqmall.mvp.model.stockTransfer;

import com.tqmall.global.BaseBean;

import java.io.Serializable;
import java.util.List;

public class StockTransferWithBatchNoBo extends BaseBean implements Serializable {

    public Integer shopId;

    public Integer warehouseId;

    public Integer goodsId;

    public String goodsSn;

    public String goodsName;

    public String goodsFormat;

    public String goodsUnit;

    public String adapterModels;

    public String oeCode;

    public String batchNo;

    public String destAllocationSn; //目的库位

    public Integer sumInstoreQty;//在库总数

    public List<StockTransferWithBatchNoEntryBo> pdaTransferAllocationBos;
}
