package com.tqmall.mvp.model.api;

import com.tqmall.global.Result;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Jay on 16/12/12.
 */

public interface StockInWareApi {

    /**
     * 根据业务单号查询该商品所在的单据(随机返回一条未上架的商品所在的单据)
     *
     * @param callback
     */
    @GET("/stock/stockInWare/queryInWareInfoByBnsBillNo")
    void queryInWareInfoByBnsBillNo(
            @Query("bnsBillNo") String bnsBillNo,
            Callback<Result<StockInWareBillBo>> callback);


    /**
     * 进入入库作业页面初始化未入库商品任务列表
     *
     * @param pageSize
     * @param pageNumber
     * @param callback
     */
    @GET("/stock/stockInWare/queryInWareGoodsInfoIndex")
    void queryInWareGoodsInfoIndex(
            @Query("pageSize") Integer pageSize,
            @Query("pageNumber") Integer pageNumber,
            Callback<Result<List<StockInWareInfo>>> callback);


    /**
     * 根据批次码去查询上架的商品以及单据信息
     *
     * @param batchNo
     * @param callback
     */
    @GET("/stock/stockInWare/queryInWareGoodsInfo")
    void queryStockInWareInfoByBatchNo(
            @Query("batchNo") String batchNo,
            Callback<Result<StockInWareInfo>> callback);


    /**
     * 入库单审核/上架操作入口
     *
     * @param stockInWareBillBo
     * @param callback
     */
    @POST("/stock/stockInWare/goodsOnShelves4PDA")
    void goodsOnShelves(
            @Body StockInWareBillBo stockInWareBillBo,
            Callback<Result<StockInWareInfo>> callback);
}
