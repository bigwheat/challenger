package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.presenter.base.MvpPresenter;

import java.util.List;

/**
 * Created by Jay on 17/1/18.
 */

public interface StockOutWareGoodsDetailPresenter extends MvpPresenter {


    /**
     * 商品出库
     *
     * @param position
     * @param stockOutInfoDetailList
     */
    void postStockOutData(List<StockOutInfoDetail> stockOutInfoDetailList, int position);


    /**
     * 查询出库列表详情
     *
     * @param id
     */
    void queryStockOutDetailInfo(Integer id, int originDataSize, boolean isRefresh, int position);

}
