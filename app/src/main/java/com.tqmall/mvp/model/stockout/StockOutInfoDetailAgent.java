package com.tqmall.mvp.model.stockout;

import com.tqmall.global.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jay on 17/1/18.
 */

public class StockOutInfoDetailAgent extends BaseBean implements Serializable{

    public List<StockOutInfoDetail> stockOutInfoDetailList;

    public Integer position;
}
