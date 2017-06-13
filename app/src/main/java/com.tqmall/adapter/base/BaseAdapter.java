package com.tqmall.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by Jay on 16/7/30.
 */

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {


    protected List<T> listData;
    protected LayoutInflater inflater;
    public Context context;

    public BaseAdapter(Context context, List<T> listData) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listData = listData;
    }


    /**
     * 返回数据源
     *
     * @return dataList
     */

    public List<T> getData() {
        return listData;
    }

    /**
     * @param list 为dataList赋值
     */
    public void setData(List<T> list) {
        this.listData = list;
        notifyDataSetChanged();
    }

    /**
     * @param list 添加list
     */
    public void addList(List<T> list) {
        this.listData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * @param layoutId
     * @return convertview
     */
    public View inflate(int layoutId) {
        return inflater.inflate(layoutId, null);
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public T getItem(int position) {
        return listData == null ? null : listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
