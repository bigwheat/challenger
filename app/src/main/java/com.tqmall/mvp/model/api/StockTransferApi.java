package com.tqmall.mvp.model.api;

import com.tqmall.global.Result;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Jay on 16/12/20.
 */

public interface StockTransferApi {


    /**
     * 通过批次码查询库存信息
     *
     * @param batchNo
     * @param callback
     */
    @GET("/shelvesStock/pda_get_shelves_stock_by_batchNo")
    void queryShelvesStockByBatch(
            @Query("batchNo") String batchNo,
            Callback<Result<StockTransferWithBatchNoBo>> callback);


    /**
     * 通过库位码查询库存信息
     *
     * @param allocationSn
     * @param callback
     */
    @GET("/shelvesStock/pda_get_shelves_stock_by_allocationSn")
    void queryShelvesStockByAllocationSn(
            @Query("allocationSn") String allocationSn,
            Callback<Result<List<StockTransferWithAllocationSnBo>>> callback);


    /**
     * 以批次码为维度移库
     *
     * @param stockTransferWithBatchNoBo
     * @param callback
     */
    @POST("/shelvesStock/pda_stock_transfer_list")
    void transferStockByBatchNo(
            @Body StockTransferWithBatchNoBo stockTransferWithBatchNoBo,
            Callback<Result<String>> callback);


    /**
     * 以库位码为维度移库
     *
     * @param stockTransferWithAllocationSnBo
     * @param callback
     */
    @POST("/shelvesStock/pda_stock_transfer_single")
    void transferStockByAllocationSn(
            @Body StockTransferWithAllocationSnBo stockTransferWithAllocationSnBo,
            Callback<Result<String>> callback);

}
