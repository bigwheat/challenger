package com.tqmall.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Jay on 16/7/30.
 */

public abstract class BaseHolderAdapter<T,H> extends BaseAdapter<T> {



    public BaseHolderAdapter(Context context, List<T> listData){
      super(context,listData);
    }

    /**
     * 返回自定义item的布局
     */
    public abstract int setItemRes();

    /**
     * @param convertView
     * @param position
     *
     *  创建一个ViewHolder
     */
    public abstract H buildHolder(View convertView,T t, int position);

    /**
     * @param holder
     * @param position
     *
     *  绑定视图
     */
    public abstract void bindView(H holder,T t, int position);


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        T t=listData.get(position);
        if(convertView==null){
            if(setItemRes()==0){
                convertView=inflater.inflate(null,parent,false);
            }else {
                convertView = inflater.inflate(setItemRes(), parent, false);
            }
            holder=buildHolder(convertView,t,position);
            convertView.setTag(holder);
        }else {
            holder=(H)convertView.getTag();
        }
        bindView(holder,t,position);
        return convertView;
    }


}
