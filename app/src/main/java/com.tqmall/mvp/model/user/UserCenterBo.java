package com.tqmall.mvp.model.user;

import com.tqmall.global.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jay on 16/12/28.
 */

public class UserCenterBo extends BaseBean implements Serializable{

    public Long id;
    public String staffNo;
    public String password;
    public String userName;
    public Long orgId;
    public String orgName;
    public List<String> roleNames;
    //        public List<RoleDto> roleDtoList;
    public String mobilePhone;
    public Boolean isOpenWms;

}
