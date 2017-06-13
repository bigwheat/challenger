package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckInfo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 17/2/7.
 */

public interface StockCheckView  extends MvpView{

    /**
     * 初始化RecycleView
     *
     * @param data
     * @param isLoadMore
     */
    void initRecycleView(List<StockCheckInfo> data , boolean isLoadMore);

    /**
     * 返回数据,传递给下一个activity
     *
     * @param data
     */
    void returnStockInfoDetailData(List<StockCheckDetailInfo> data);

    /**
     * 显示没有盘点任务的页面
     */
    void noData();

    /**
     * 显示没有网络的页面
     */
    void noNetWork();

    /**
     * 刷新页面
     */
    void showRefresh();

    /**
     * 隐藏刷新
     */
    void hideRefresh();

}
