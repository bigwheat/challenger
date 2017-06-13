package com.tqmall.mvp.model.stockout;

import com.tqmall.global.BaseBean;

import java.io.Serializable;

/**
 * Created by Jay on 17/1/18.
 */

public class StockOutInfoDetail extends BaseBean implements Serializable{

    public Integer id;//出库单Id

    public Integer warehouseBillEntryId;//出库明细Id

    public String warehouseBillNo; //出库单单号

    public Integer bnsBillId; //业务单Id

    public String bnsBillNo; //业务单单号

    public Integer ShopId; // 组织Id

    public Integer warehouseId; // 仓库Id

    public Integer oppositeId; //对方Id

    public String oppositeName; //对方名称

    public Integer bnsBillType; // 业务单类型

    public Integer goodsId; //商品id

    public String goodsSn;//商品编码

    public String goodsName; //商品名称

    public String goodsFormat; //商品规格

    public String goodsUnit; //计量单位

    public String adapterModels; //适配车型

    public String oeCode; //oe码

    public String batchNo; //批次号

    public String lockAllocationSn;// 锁定库位

    public String  allocationSn; //实际出库库位

    public Integer oppositeType;//对方类型

    public Integer stockOutQty; //提交的出库数量

    public Integer lockQty; // 库位锁定数量(库位应拣数量)

    public Integer releaseQty; //库位已拣数量

    public Integer pickSortNo;//拣货路径
}
