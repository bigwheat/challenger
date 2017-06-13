package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.model.stockout.StockOutWareBillBo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 17/1/16.
 */

public interface StockOutWareView extends MvpView {

    /**
     * 初始化RecycleView
     *
     * @param data
     * @param isLoadMore
     */
    void initRecycleView(List<StockOutWareBillBo> data , boolean isLoadMore);

    /**
     * 返回数据,传递给下一个activity
     *
     * @param data
     */
    void returnStockInfoDetailData(List<StockOutInfoDetail> data);

    /**
     * 显示没有出库任务的页面
     */
    void noData();

    /**
     * 显示没有网络的页面
     */
    void noNetWork();

    void showRefresh();

    void hideRefresh();
}
