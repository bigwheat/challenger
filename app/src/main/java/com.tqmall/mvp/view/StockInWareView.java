package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 16/12/12.
 */

public interface StockInWareView extends MvpView {

    /**
     * 初始化RecycleView
     *
     * @param stockInWareBillBo
     */
    void initAllViews(StockInWareBillBo stockInWareBillBo);

    void showRefresh();

    void hideRefresh();

}
