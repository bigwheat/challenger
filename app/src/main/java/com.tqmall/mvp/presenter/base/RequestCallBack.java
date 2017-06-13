package com.tqmall.mvp.presenter.base;

/**
 * Created by Jay on 16/11/29.
 */

public interface RequestCallBack<D> {

    void beforeRequest();

    void onSuccess(D data);

    void onError(String errorMsg);

}
