package com.tqmall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.mvp.model.stockcheck.StockCheckInfo;
import com.tqmall.mvp.widget.CircleProgressView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jay on 17/02/08.
 */

public class StockCheckWareListAdapter extends BaseRecyclerListAdapter<StockCheckInfo, StockCheckWareListAdapter.ViewHolder> {


    @Inject
    public StockCheckWareListAdapter() {

    }

    @Override
    protected StockCheckWareListAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_check_list_item, viewGroup, false);
        return new StockCheckWareListAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(StockCheckWareListAdapter.ViewHolder viewHolder, int position) {

        StockCheckInfo stockCheckInfo = dataList.get(position);
        if (stockCheckInfo != null) {
            viewHolder.tvWarehouseBillNo.setText(stockCheckInfo.warehouseBillNo);
            viewHolder.tvEntriesCount.setText(stockCheckInfo.entriesCount == null ? "" : (stockCheckInfo.entriesCount + ""));
            viewHolder.tvCheckedEntriesCount.setText(stockCheckInfo.checkedEntriesCount == null ? "" : (stockCheckInfo.checkedEntriesCount + ""));

            try {
                if (stockCheckInfo.checkedEntriesCount != null && stockCheckInfo.entriesCount != null) {
                    //设置比率
                    int rate = stockCheckInfo.checkedEntriesCount * 100 / stockCheckInfo.entriesCount;
                    viewHolder.circleProgressView.setProgress(rate);
                }
            } catch (Exception e) {
                viewHolder.circleProgressView.setProgress(0);
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvWarehouseBillNo)
        TextView tvWarehouseBillNo;

        @BindView(R.id.tvEntriesCount)
        TextView tvEntriesCount;

        @BindView(R.id.tvCheckedEntriesCount)
        TextView tvCheckedEntriesCount;

        @BindView(R.id.circleProgressbar)
        CircleProgressView circleProgressView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

