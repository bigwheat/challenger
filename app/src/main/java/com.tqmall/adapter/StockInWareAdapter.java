package com.tqmall.adapter;


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tqmall.R;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.mvp.model.stockInware.StockInWareBillEntryBo;
import com.tqmall.mvp.widget.CircleProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jay on 16/12/13.
 */

public class StockInWareAdapter extends BaseRecyclerListAdapter<StockInWareBillEntryBo,StockInWareAdapter.ViewHolder> {

    @Override
    protected StockInWareAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_in_ware_detail_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(StockInWareAdapter.ViewHolder viewHolder, int position) {

        StockInWareBillEntryBo stockInWareBillEntryBo=dataList.get(position);
        if(stockInWareBillEntryBo!=null){
            viewHolder.ivNewGoods.setVisibility(View.GONE);
            viewHolder.tvGoodsName.setText(stockInWareBillEntryBo.goodsName);
            viewHolder.tvInQty.setText(stockInWareBillEntryBo.inQty+"");
            viewHolder.tvOnShelveQty.setText(stockInWareBillEntryBo.onShelvesQty+"");
            viewHolder.tvAdapterModels.setText(stockInWareBillEntryBo.adaptModels);
            viewHolder.tvVirtualAllocationSn.setText(stockInWareBillEntryBo.virtualAllocationSn);

            if (TextUtils.isEmpty(stockInWareBillEntryBo.virtualAllocationSn)
                    ||((stockInWareBillEntryBo.virtualAllocationSn).equals(""))) {
                viewHolder.ivNewGoods.setVisibility(View.VISIBLE);
            }

            try {
                if(stockInWareBillEntryBo.inQty!=null&&stockInWareBillEntryBo.onShelvesQty!=null){
                    //设置比率
                    int rate=stockInWareBillEntryBo.onShelvesQty*100/stockInWareBillEntryBo.inQty;
                    viewHolder.circleProgressView.setProgress(rate);
                }
            }catch (Exception e){
                viewHolder.circleProgressView.setProgress(0);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;

        @BindView(R.id.tvVirtualAllocationSn)
        TextView tvVirtualAllocationSn;

        @BindView(R.id.tvAdapterModels)
        TextView tvAdapterModels;

        @BindView(R.id.circleProgressbar)
        CircleProgressView circleProgressView;

        @BindView(R.id.tvInQty)
        TextView tvInQty;

        @BindView(R.id.tvOnShelveQty)
        TextView tvOnShelveQty;

        @BindView(R.id.ivNewGoods)
        ImageView ivNewGoods;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
