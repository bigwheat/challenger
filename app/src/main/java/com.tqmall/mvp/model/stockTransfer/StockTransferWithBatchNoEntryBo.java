package com.tqmall.mvp.model.stockTransfer;


import com.tqmall.global.BaseBean;

import java.io.Serializable;

public class StockTransferWithBatchNoEntryBo extends BaseBean implements Serializable{

    public Integer stockShelvesId;

    public Integer instoreQty;//在库数量

    public Integer areaId;

    public String areaSn;

    public Integer allocationId;

    public String allocationSn;//源库位编码

    public Integer changeQty;//移库数量
}
