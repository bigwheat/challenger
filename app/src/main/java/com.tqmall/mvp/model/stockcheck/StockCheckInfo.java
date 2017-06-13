package com.tqmall.mvp.model.stockcheck;


import com.tqmall.global.BaseBean;

import java.io.Serializable;

/**
 * Created by Jay on 17/2/8.
 */

public class StockCheckInfo extends BaseBean implements Serializable{

    //单据id
    public Integer id;

    //盘点单号
    public String warehouseBillNo;

    //盘点计划状态  1.开始盘点  2.继续盘点
    public Integer warehouseBillStatus;

    //已盘点数量
    public Integer checkedEntriesCount;

    //应盘点数量
    public Integer entriesCount;

}
