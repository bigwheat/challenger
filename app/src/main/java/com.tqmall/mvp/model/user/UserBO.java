package com.tqmall.mvp.model.user;

import java.io.Serializable;

public class UserBO implements Serializable {

    public Long userId;

    public String userName;//用户名

    public Long orgId;//shopId

    public String orgPro;//部门

    public Integer warehouseId; //partErp 转成 part的 gm_warehouse_id

    public String warehouseNo;

    public String warehouseName;

    public String orgName;//公司名称

    public String roleName;//角色

    public String roleDescription;

}
