package com.tqmall.global;

import java.io.Serializable;

/**
 * Created by Jay on 16/8/15.
 */

public class Result<D> extends BaseBean implements Serializable {

    public boolean success;
    public String code;
    public String message;
    public D data;
}
