package com.tqmall.mvp.model.stockTransfer;


import com.tqmall.global.BaseBean;

import java.io.Serializable;

public class StockTransferWithAllocationSnBo extends BaseBean implements Serializable{

    public Integer stockShelvesId;

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

    public Integer instoreQty;

    public Integer areaId;

    public String areaSn;

    public Integer allocationId;

    public String allocationSn;

    public String destAllocationSn; //对方库位编码

    public Integer changeQty; //移库数量
}
