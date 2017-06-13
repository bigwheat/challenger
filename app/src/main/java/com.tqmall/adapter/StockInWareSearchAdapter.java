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
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.widget.CircleProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jay on 16/12/19.
 */

public class StockInWareSearchAdapter extends BaseRecyclerListAdapter<StockInWareInfo, StockInWareSearchAdapter.ViewHolder> {

    @Override
    protected StockInWareSearchAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_in_ware_detail_item, viewGroup, false);
        return new StockInWareSearchAdapter.ViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_cell, viewGroup, false);
        return new FooterViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(StockInWareSearchAdapter.ViewHolder viewHolder, int position) {

        StockInWareInfo stockInWareInfo = dataList.get(position);
        if (stockInWareInfo != null) {
            viewHolder.ivNewGoods.setVisibility(View.GONE);
            viewHolder.tvGoodsName.setText(stockInWareInfo.goodsName);
            viewHolder.tvInQty.setText(stockInWareInfo.inQty + "");
            viewHolder.tvOnShelveQty.setText(stockInWareInfo.onShelvesQty + "");
            viewHolder.tvAdapterModels.setText(stockInWareInfo.adaptModels);
            viewHolder.tvVirtualAllocationSn.setText(stockInWareInfo.virtualAllocationSn);
            if (TextUtils.isEmpty(stockInWareInfo.virtualAllocationSn) || ("".equals(stockInWareInfo.virtualAllocationSn))) {
                viewHolder.ivNewGoods.setVisibility(View.VISIBLE);
            }

            try {
                if (stockInWareInfo.inQty != null && stockInWareInfo.onShelvesQty != null) {
                    //设置比率
                    int rate = stockInWareInfo.onShelvesQty * 100 / stockInWareInfo.inQty;
                    viewHolder.circleProgressView.setProgress(rate);
                }
            } catch (Exception e) {
                viewHolder.circleProgressView.setProgress(0);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

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
            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

