package com.tqmall.adapter.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Jay on 16/12/24.
 */

public abstract class BasePagerAdapter<T, H> extends PagerAdapter {


    /**
     * 数据源
     */
    public List<T> data;

    /**
     * 上下文
     */
    public Context context;

    /**
     * view控制索引
     */
    protected H holder;


    /**
     * 构造器
     *
     * @param context
     * @param data
     */
    public BasePagerAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
    }

    /**
     * 获取当前页的布局索引
     *
     * @return
     */
    protected abstract H viewHolder(View view,List<T> data,int position);


    /**
     * 设置资源ID
     *
     * @return
     */
    protected abstract int setResId();


    /**
     * 初始化viewpager的属性
     */
    protected abstract void initPageView(H holder, T t, int position, List<T> data);


    /**
     * 设置数据源
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 返回数据
     *
     * @return
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 清空数据
     */
    public void clearData() {
        if (data != null && data.size() > 0) {
            data.clear();
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, setResId(), null);
        holder = viewHolder(view,data,position);
        initPageView(holder, data.get(position), position,data);
        setPostData(holder,data,position);
        container.addView(view);
        return view;
    }

    protected void setPostData(H h,List<T> data,int position){

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
