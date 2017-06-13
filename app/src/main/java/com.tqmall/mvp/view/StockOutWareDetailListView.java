package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.view.base.MvpView;

import java.util.List;


/**
 * Created by Jay on 17/1/16.
 */

public interface StockOutWareDetailListView extends MvpView {

    void showRefresh();

    void hideRefresh();

    void initRecycleView(List<StockOutInfoDetail> stockOutInfoDetailList);
}
