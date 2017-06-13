package com.tqmall.mvp.model.stockInware;

import com.tqmall.global.BaseBean;

import java.io.Serializable;

/**
 * Created by Jay on 16/12/12.
 */

public class StockInWareBillEntryBo extends BaseBean implements Serializable{

    public Integer shopId; //门店Id

    public Integer warehouseId; // 仓库Id

    public Integer goodsId; //商品Id

    public String goodsSn; // 商品编码

    public String goodsName; //商品名称

    public String goodsUnit; //计量单位

    public String goodsFormat; //规格型号

    public String adaptModels; //适配车型

    public String oeCode; //oe码

    public Integer inQty; //入库数量

    public Double inPrice; //入库单价

    public Integer warehouseBillId; //入库单id

    public Integer bnsSaleBillId; //被退货的销售订单Id(退货单必填)

    public String bnsSaleBillNo; // 被退货的销售订单号(退货单必填)

    //========⬆️必填 ⬇️非必填========//

    public Integer goodsNumber;//总数量

    public Integer inWareNumber;//已到数量

    public Integer id;

    public String isDeleted;

    public String gmtCreate;

    public String gmtModified;

    public Integer creator;

    public Integer modifier;

    public String warehouseBillNo; //入库单号

    public Integer onShelvesQty; //上架数量

    public Integer isPrintLabel; // 是否打码

    public String productionDate; //生产日期

    public String validTill; //有效期至

    public String batchNo; //批次码

    public Integer areaId;

    public String areaSn;

    public Integer allocationId;

    public String allocationSn;//实际库位号

    public String virtualAllocationSn;// 推荐库位号

    public String goodsType;//商品类型 0:主库 1:外库
}
