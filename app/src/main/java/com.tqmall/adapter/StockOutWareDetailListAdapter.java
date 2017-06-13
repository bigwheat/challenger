package com.tqmall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.widget.CircleProgressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jay on 16/12/19.
 */

public class StockOutWareDetailListAdapter extends BaseRecyclerListAdapter<StockOutInfoDetail, StockOutWareDetailListAdapter.ViewHolder> {


    @Inject
    public StockOutWareDetailListAdapter() {

    }

    @Override
    protected StockOutWareDetailListAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_out_ware_detail_list_item, viewGroup, false);
        return new StockOutWareDetailListAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(StockOutWareDetailListAdapter.ViewHolder viewHolder, int position) {

        StockOutInfoDetail stockOutInfoDetail = dataList.get(position);
        if (stockOutInfoDetail != null) {
            viewHolder.tvGoodsName.setText(stockOutInfoDetail.goodsName);
            viewHolder.tvOutQty.setText(stockOutInfoDetail.lockQty == null ? "" : (stockOutInfoDetail.lockQty + ""));
            viewHolder.tvPrepareQty.setText(stockOutInfoDetail.releaseQty == null ? "" : (stockOutInfoDetail.releaseQty + ""));
            viewHolder.tvAdapterModels.setText(stockOutInfoDetail.adapterModels == null ? "" : stockOutInfoDetail.adapterModels + "");
            viewHolder.tvLockAllocationSn.setText(stockOutInfoDetail.lockAllocationSn == null ? "" : stockOutInfoDetail.lockAllocationSn + "");

            try {
                if (stockOutInfoDetail.lockQty != null && stockOutInfoDetail.releaseQty != null) {
                    //设置比率
                    int rate = stockOutInfoDetail.releaseQty * 100 / stockOutInfoDetail.lockQty;
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

        @BindView(R.id.tvLockAllocationSn)
        TextView tvLockAllocationSn;

        @BindView(R.id.tvAdapterModels)
        TextView tvAdapterModels;

        @BindView(R.id.circleProgressbar)
        CircleProgressView circleProgressView;

        @BindView(R.id.tvOutQty)
        TextView tvOutQty;

        @BindView(R.id.tvPrepareQty)
        TextView tvPrepareQty;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

