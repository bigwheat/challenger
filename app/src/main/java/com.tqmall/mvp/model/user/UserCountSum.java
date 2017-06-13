package com.tqmall.mvp.model.user;

import com.tqmall.global.BaseBean;

import java.io.Serializable;

public class UserCountSum extends BaseBean implements Serializable {
    public Integer prepare;
    public Integer checkIn;
    public Integer count;
    public Integer checkOut;
    public Integer goodsShelves;
}
