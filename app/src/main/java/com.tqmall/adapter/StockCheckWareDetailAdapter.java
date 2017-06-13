package com.tqmall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.global.App;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckInfo;
import com.tqmall.mvp.widget.CircleProgressView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jay on 17/02/08.
 */

public class StockCheckWareDetailAdapter extends BaseRecyclerListAdapter<StockCheckDetailInfo, StockCheckWareDetailAdapter.ViewHolder> {


    StockCheckWareDetailAdapterCallBack stockCheckWareDetailAdapterCallBack;

    @Inject
    public StockCheckWareDetailAdapter() {

    }

    @Override
    protected StockCheckWareDetailAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_check_list_detail_item, viewGroup, false);
        return new StockCheckWareDetailAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(final StockCheckWareDetailAdapter.ViewHolder viewHolder, final int position) {

        StockCheckDetailInfo stockCheckDetailInfo = dataList.get(position);
        if (stockCheckDetailInfo != null) {
            viewHolder.tvGoodsName.setText(stockCheckDetailInfo.goodsName == null ? "" : stockCheckDetailInfo.goodsName);
            viewHolder.tvAdapterModels.setText(stockCheckDetailInfo.adapterModels == null ? "" : (stockCheckDetailInfo.adapterModels + ""));
            viewHolder.tvCheckedEntriesCount.setText(stockCheckDetailInfo.checkAfterQty == null ? "" : (stockCheckDetailInfo.checkAfterQty + ""));
            viewHolder.tvAllocationSn.setText(stockCheckDetailInfo.allocationSn == null ? "" : stockCheckDetailInfo.allocationSn);
            if (stockCheckDetailInfo.checkAfterQty == null) {
                viewHolder.ivGoodsType.setBackgroundColor(App.mContext.getResources().getColor(R.color.darkgray));
                viewHolder.tvIsChecked.setText("未盘点");
            } else if (stockCheckDetailInfo.checkAfterQty != null) {
                viewHolder.ivGoodsType.setBackgroundColor(App.mContext.getResources().getColor(R.color.colorPrimary));
                viewHolder.tvIsChecked.setText("已盘点");
            }
            if (dataList.size() == 1) {
                viewHolder.ivGoodsType.setBackgroundColor(App.mContext.getResources().getColor(R.color.red_little));
            }

            if (stockCheckWareDetailAdapterCallBack != null) {
                viewHolder.rlDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stockCheckWareDetailAdapterCallBack.setOnItemDetailClick(viewHolder.rlDetail,dataList, position);
                    }
                });

                viewHolder.rlCheckedEntriesCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stockCheckWareDetailAdapterCallBack.setOnItemPostDataClick(viewHolder.rlCheckedEntriesCount,dataList, position);
                    }
                });
            }

        }
    }

    public interface StockCheckWareDetailAdapterCallBack {

        void setOnItemDetailClick(View view, List<StockCheckDetailInfo> data, int position);

        void setOnItemPostDataClick(View view, List<StockCheckDetailInfo> data, int position);
    }

    public void setStockCheckWareDetailAdapterCallBack(StockCheckWareDetailAdapterCallBack stockCheckWareDetailAdapterCallBack) {
        this.stockCheckWareDetailAdapterCallBack = stockCheckWareDetailAdapterCallBack;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;

        @BindView(R.id.tvAdapterModels)
        TextView tvAdapterModels;

        @BindView(R.id.tvCheckedEntriesCount)
        TextView tvCheckedEntriesCount;

        @BindView(R.id.tvAllocationSn)
        TextView tvAllocationSn;

        @BindView(R.id.ivGoodsType)
        ImageView ivGoodsType;

        @BindView(R.id.rlDetail)
        RelativeLayout rlDetail;

        @BindView(R.id.rlCheckedEntriesCount)
        RelativeLayout rlCheckedEntriesCount;

        @BindView(R.id.tvIsChecked)
        TextView tvIsChecked;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

