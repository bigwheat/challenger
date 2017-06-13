package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 16/12/19.
 */

public interface SearchStockInView extends MvpView {


    /**
     * 返回数据到Activity
     *
     * @param stockInWareInfo
     */
     void returnData(StockInWareInfo stockInWareInfo);

}
