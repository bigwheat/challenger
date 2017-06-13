package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.view.base.MvpView;

/**
 * Created by Jay on 16/12/15.
 */

public interface StockInWareDetailView extends MvpView {

    /**
     * 初始化布局
     */
    void initViews(StockInWareInfo stockInWareInfo);

    /**
     * 刷新页面
     */
    void refreshViews(StockInWareInfo stockInWareInfo,String code,String message);
}
