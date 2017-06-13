package com.tqmall.enmus;

/**
 * Created by Jay on 16/12/13.
 */

public enum BillTypeEnum implements KeyService {

    SALE_ORDER(1, "销售订单"),
    TRANSFER_ORDER(3, "调拨订单"),
    PURCHASE_BILL(5, "采购订单"),
    SALE_RETURN_EXCHANGE_BILL(13, "销售退换货"),
    PURCHASE_RETURN_EXCHANGE_BILL(14, "采购退换货"),
    ORDER_ADJUST_BILL(15, "调整单"),
    INVENTORY_CHECK_BILL(26, "仓库盘点单"),
    INVENTORY_TRANSFER_BILL(27, "仓库移库单"),

    // 销售订单从主库出,入库、出库、批次、库位使用
    SALE_OUT_FROM_MAIN(28, "从主库出销售订单"),
    ALL_IN_WARE_END(9,"全部入库"),
    PARTIAL_IN_WARE(8,"部分入库"),
    //出库的对方名称类型
    STOCK_OUT_WAREHOUSE(3,"仓库"),
    STOCK_OUT_CUSTOMER(1,"客户"),
    STOCK_OUT_PROVIDER(2,"供应商"),

    //以下为待清理的单据类型，请在新的业务中不要使用

    SALE_OUT_WARE_BILL(2, "销售出库单"),
    PURCHASE_IN_WARE_BILL(7, "采购入库订单");



    private int key;

    private String name;


    BillTypeEnum(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public static BillTypeEnum getEnum(int key) {
        for (BillTypeEnum item : BillTypeEnum.values()) {
            if (item.key == key) {
                return item;
            }
        }
        return null;
    }

    public static String getName(int key) {
        BillTypeEnum item = getEnum(key);
        return item != null ? item.name : null;
    }


    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }
}
