package com.tqmall.mvp.model.stockInware;


import com.tqmall.global.BaseBean;
import java.io.Serializable;

public class StockInWareInfo extends BaseBean implements Serializable{

    public Integer warehouseBillId; // 入库单Id  TODO

    public String warehouseBillNo; // 入库单号

    public Integer bnsBillId; // 业务单Id

    public String bnsBillNo; // 业务单号

    public Integer bnsBillType; //业务单类型

    public Integer warehouseEntryId; //入库明细Id  TODO  明细的ID

    public Integer goodsId; //商品Id   TODO

    public String goodsSn; //商品Sn

    public String goodsName; //商品名称

    public String goodsUnit; // 计量单位

    public String goodsFormat; //规格型号

    public String adaptModels; // 适配车型

    public String oeCode; // oe码

    public Integer inQty; //已经入库数量

    public Integer onShelvesQty; //需要上架数量  TODO

    public String batchNo;  // 批次码

    public String allocationSn;//实际库位号  TODO

    public String virtualAllocationSn;// 推荐库位号

    public String oppositeName; //对方名称   //TODO

}
