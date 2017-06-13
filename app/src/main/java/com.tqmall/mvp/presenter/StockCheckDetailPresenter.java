package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.presenter.base.MvpPresenter;

import java.util.List;

/**
 * Created by Jay on 17/2/8.
 */

public interface StockCheckDetailPresenter extends MvpPresenter {


    /**
     * 查询盘点列表商品详情
     *
     * @param pageNumber
     * @param pageSize
     * @param inventoryCheckPlanId
     * @param allocationSn
     * @param batchNo
     * @param isLoadMore
     */
    void queryStockCheckDetailInfo(Integer pageNumber, Integer pageSize, Integer inventoryCheckPlanId,
                                   String allocationSn,String batchNo,boolean isLoadMore);


    /**
     * 提交盘点数据
     *
     * @param originData
     * @param originPos
     */
    void postStockCheckData(List<StockCheckDetailInfo> originData, int originPos, List<StockCheckDetailInfo> noChangeData);

}
