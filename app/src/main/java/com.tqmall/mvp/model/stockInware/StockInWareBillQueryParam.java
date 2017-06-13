package com.tqmall.mvp.model.stockInware;

import com.tqmall.global.BaseBean;

import java.io.Serializable;

/**
 * Created by Jay on 16/12/12.
 */

public class StockInWareBillQueryParam extends BaseBean implements Serializable{

    public Integer shopId;//shopId

    public Integer warehouseId;//仓库Id

    public String bnsBillCreateTimeStart; //下单开始时间

    public String bnsBillCreateTimeEnd;//下单结束时间

    public String warehouseBillNo; //入库单单号

    public Integer warehouseBillStatus;//入库单状态

    public Integer bnsBillId;//业务单据号id

    public String bnsBillNo;//业务单据号

    public Integer bnsBillType;//入库单单据类型

    public Integer bnsBillStatus;//入库单单据状态

    public Integer oppositeId; //对方Id(客户、供应商)

    public String oppositeName; //对方名称(客户名称,供应商名称)

    public String goodsId; //商品id

    public String goodsSn;//商品编码

    public String goodsName; //商品名称

    public String oeCode; //oe码
}
