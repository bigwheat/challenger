package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 17/2/8.
 */

public interface StockCheckDetailView extends MvpView {

    /**
     * 初始化RecycleView
     *
     * @param data
     * @param isLoadMore
     */
    void initRecycleView(List<StockCheckDetailInfo> data , boolean isLoadMore);


    /**
     * 提交完成盘点数据后刷新RecycleView
     *
     * @param originData
     * @param originPos
     * @param newData
     */
    void initRecycleViewAfterPostData(List<StockCheckDetailInfo> originData, int originPos,StockCheckDetailInfo newData, List<StockCheckDetailInfo> noChangeData,boolean isSuccess);


    /**
     * 显示没有数据的页面
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
