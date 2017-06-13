package com.tqmall.mvp.model.api;

import com.tqmall.global.Result;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.model.stockout.StockOutWareBillBo;
import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Jay on 17/1/16.
 */

public interface StockOutApi {


    /**
     * 分页查询未出库列表
     *
     * @param pageSize
     * @param pageNumber
     * @param callback
     */
    @GET("/stock/out/getOutBillInfo")
    void queryStockOutInfo(
            @Query("pageSize") Integer pageSize,
            @Query("pageNumber") Integer pageNumber,
            Callback<Result<List<StockOutWareBillBo>>> callback);


    /**
     * 查询未出库商品详情
     *
     * @param id
     * @param callback
     */
    @GET("/stock/out/getOutDetailInfo")
    void queryStockOutDetailInfo(
            @Query("id") Integer id,
            Callback<Result<List<StockOutInfoDetail>>> callback);



    @POST("/stock/out/goodsStockOut4PDA")
    void postStockOutData(
            @Body StockOutInfoDetail stockOutInfoDetail,
            Callback<Result<String>> callback);

}
