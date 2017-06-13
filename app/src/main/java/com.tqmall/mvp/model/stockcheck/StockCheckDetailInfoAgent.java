package com.tqmall.mvp.model.stockcheck;

import com.tqmall.global.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jay on 17/2/8.
 */

public class StockCheckDetailInfoAgent extends BaseBean implements Serializable{

    public List<StockCheckDetailInfo> stockCheckDetailInfos;

    public Integer position;

}
