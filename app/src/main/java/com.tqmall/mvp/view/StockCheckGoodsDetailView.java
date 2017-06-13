package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.view.base.MvpView;

/**
 * Created by Jay on 17/2/9.
 */

public interface StockCheckGoodsDetailView extends MvpView {

    void initView(StockCheckDetailInfo stockCheckDetailInfo);

}
