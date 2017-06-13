package com.tqmall.mvp.model.stockInware;

import com.tqmall.global.BaseBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 16/12/12.
 */

public class StockInWareBillBo extends BaseBean implements Serializable{

    public Integer shopId; //门店Id

    public Integer warehouseId; //仓库Id

    public Integer bnsBillType; //业务单据类型(采购订单、销售退货单、调整单、销售单(从主库出))

    public String bnsTypeName;//

    public Integer bnsBillId; //业务单ID(采购订单、销售退货单、调整单)

    public String bnsBillNo; //业务单号(采购订单、销售退货单、调整单)

    public String gmtBnsBillCreate; // 业务单创建时间

    public Integer oppositeType; //对方类型(供应商、客户、仓库)

    public Integer oppositeId; //对方Id

    public String oppositeName; //对方名称

    public Integer provinceId; // 省

    public String provinceName;

    public Integer cityId; //市

    public String cityName;

    public Integer districtId; //区

    public String districtName;

    public Integer streetId; //街道

    public String streetName;

    public String oppositeAddress; //对方详细地址

    public Integer isQuickIn; //是否一键入库 0-否 1-是

    //========⬆️必填 ⬇️非必填========//

    public Integer id;

    public String isDeleted;

    public String gmtCreate;

    public String gmtModified;

    public Integer creator;

    public String creatorName;

    public Integer modifier;

    public String oppositeSn; //对方编号

    public Double inwareAmount; //入库金额

    public String warehouseBillNo; //入库单号

    public Integer warehouseBillStatus; //入库单状态

    public String warehouseBillStatusName;//入库单状态

    public Integer auditorId; //审核人

    public String gmtAudit; //审核时间

    public Integer isPushPda; //是否推送pda

    public String warehouseBillRemarks; //备注(采购订单、销售退货单、调整单)

    public List<StockInWareBillEntryBo> inWareBillEntryBoList = new ArrayList<>();

    public String imgUrl;//入库单审核,条码打印,显示二维码路径

    public String printerName;// 入库单打印人名称
}
