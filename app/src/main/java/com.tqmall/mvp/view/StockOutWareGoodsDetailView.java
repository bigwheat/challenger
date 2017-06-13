package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;

/**
 * Created by Jay on 17/1/18.
 */

public interface StockOutWareGoodsDetailView extends MvpView {

    void initViewPage(List<StockOutInfoDetail> stockOutInfoDetailList,int originPosition,int originDataSize, boolean isRefresh);

    void dealPosition(List<StockOutInfoDetail> stockOutInfoDetailList, int position,int size);

}
