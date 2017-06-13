package com.tqmall.mvp.model.stockcheck;

import com.tqmall.global.BaseBean;

import java.io.Serializable;

/**
 * Created by Jay on 17/2/8.
 */

public class StockCheckDetailInfo extends BaseBean implements Serializable {

    public Integer id;

    //盘点单ID
    public Integer warehouseBillId;

    //盘点单单号
    public String warehouseBillNo;

    //库位库存id
    public Integer shelvesStockId;

    //库区Id
    public Integer areaId;

    //库区编码
    public String areaSn;

    //库位Id
    public Integer allocationId;

    //库位编码
    public String allocationSn; //TODO

    //商品ID
    public Integer goodsId;  //TODO

    //商品SN
    public String goodsSn;

    //商品名称
    public String goodsName;

    //商品规格
    public String goodsFormat;

    //适配车型
    public String adapterModels;

    //oe码
    public String oeCode;

    //批次码
    public String batchNo;

    //盘点前数量
    public Integer checkBeforeQty;

    //盘点后数量
    public Integer checkAfterQty; //TODO

    //库存变化数量
    public Integer changeQty;

    //备注
    public String checkRemarks;

    //单位
    public String goodsUnit;

    //应操作数
    public Integer checkedEntriesCount;

    //已操作数
    public Integer entriesCount;
}
