package com.tqmall.mvp.model.api;


import com.tqmall.global.Result;
import com.tqmall.mvp.model.Page;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckInfo;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Jay on 17/2/8.
 */

public interface StockCheckApi {

    /**
     * 分页查询盘点列表(首次进入列表页)
     *
     * @param page
     * @param size
     * @param callback
     */
    @GET("/inventory_check/app_inventory_check_plans")
    void queryStockCheckInfo(
            @Query("size") Integer size,
            @Query("page") Integer page,
            Callback<Result<Page<StockCheckInfo>>> callback);


    /**
     * 分页查询盘点详情(扫码后[包括库位码,批次码])
     *
     * @param size
     * @param page
     * @param callback
     * @param allocationSn
     */
    @GET("/inventory_check/app_inventory_check")
    void queryStockCheckDetailInfo(
            @Query("size") Integer size,
            @Query("page") Integer page,
            @Query("allocationSn") String allocationSn,
            @Query("batchNo") String batchNo,
            @Query("inventoryCheckPlanId") Integer inventoryCheckPlanId,
            Callback<Result<Page<StockCheckDetailInfo>>> callback);


    /**
     * 手动提交盘点数据()
     *
     * @param updateEntry
     * @param callback
     */
    @POST("/inventory_check/app_inventory_check_input")
    void postStockCheckData(
            @Body StockCheckDetailInfo updateEntry,
            Callback<Result<StockCheckDetailInfo>> callback);



}
