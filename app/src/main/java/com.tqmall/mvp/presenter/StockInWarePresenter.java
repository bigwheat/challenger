package com.tqmall.mvp.presenter;

import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.model.stockInware.StockInWareBillEntryBo;
import com.tqmall.mvp.presenter.base.MvpPresenter;

import java.util.List;

/**
 * Created by Jay on 16/12/12.
 */

public interface StockInWarePresenter extends MvpPresenter {



    /**
     * 查询入库单明细
     *
     * @return
     */
    void queryInWareBillEntryList(String bnsBillNo, boolean isAuto);



}
