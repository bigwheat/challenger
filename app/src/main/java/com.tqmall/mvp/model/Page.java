package com.tqmall.mvp.model;


import com.tqmall.global.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 分页
 *
 * @param <T>
 */
public class Page<T> extends BaseBean implements Serializable {

    public List<T> content;

    public Boolean last;// true;

    public Integer totalPages;

    public Integer totalElements;

    public Boolean firstPage;

    public Boolean lastPage;

    public Integer numberOfElements;

    public Boolean first;

    public Integer size;

    public Integer numb;
}
