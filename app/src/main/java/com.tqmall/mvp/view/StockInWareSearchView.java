package com.tqmall.mvp.view;

import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.view.base.MvpView;
import java.util.List;

/**
 * Created by Jay on 16/12/19.
 */

public interface StockInWareSearchView extends MvpView {

     void initRecycle(List<StockInWareInfo> stockInWareInfo,boolean isLoadMore);

     void returnData(StockInWareInfo stockInWareInfo);

     void loadMoreData(List<StockInWareInfo> stockInWareInfo,boolean isLoadMore);

     void noNetWork();

     void noData();

     void showRefresh();

     void hideRefresh();
}
