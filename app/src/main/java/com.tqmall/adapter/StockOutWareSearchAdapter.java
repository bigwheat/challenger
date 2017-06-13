package com.tqmall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tqmall.R;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.enmus.BillTypeEnum;
import com.tqmall.mvp.model.stockout.StockOutWareBillBo;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jay on 16/12/19.
 */

public class StockOutWareSearchAdapter extends BaseRecyclerListAdapter<StockOutWareBillBo, StockOutWareSearchAdapter.ViewHolder> {

    @Inject
    public StockOutWareSearchAdapter() {

    }

    @Override
    protected StockOutWareSearchAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_out_ware_detail_item, viewGroup, false);
        return new StockOutWareSearchAdapter.ViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_cell, viewGroup, false);
        return new StockOutWareSearchAdapter.FooterViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(StockOutWareSearchAdapter.ViewHolder viewHolder, int position) {

        StockOutWareBillBo stockOutWareBillBo = dataList.get(position);
        if (stockOutWareBillBo != null && stockOutWareBillBo.bnsBillType != null) {
            //单据类型
            if (stockOutWareBillBo.bnsBillType!=null && stockOutWareBillBo.bnsBillType == BillTypeEnum.SALE_ORDER.getKey()) {
                viewHolder.tvBnsBillType.setText("销售单");
                viewHolder.ivBnsBillType.setImageResource(R.mipmap.iv_sale);
            } else if (stockOutWareBillBo.bnsBillType!=null && stockOutWareBillBo.bnsBillType == BillTypeEnum.PURCHASE_RETURN_EXCHANGE_BILL.getKey()) {
                viewHolder.tvBnsBillType.setText("采购退货单");
                viewHolder.ivBnsBillType.setImageResource(R.mipmap.iv_return);
            }
            //单据号
            viewHolder.tvBnsBillNo.setText(stockOutWareBillBo.bnsBillNo + "");
            //对方名称
            viewHolder.tvOppositeName.setText(stockOutWareBillBo.oppositeName + "");
            //对方类型
            if (stockOutWareBillBo.oppositeType!=null && stockOutWareBillBo.oppositeType == BillTypeEnum.STOCK_OUT_CUSTOMER.getKey()) {
                viewHolder.tvOppositeType.setText(BillTypeEnum.STOCK_OUT_CUSTOMER.getName());
            } else if (stockOutWareBillBo.oppositeType!=null && stockOutWareBillBo.oppositeType == BillTypeEnum.STOCK_OUT_PROVIDER.getKey()) {
                viewHolder.tvOppositeType.setText(BillTypeEnum.STOCK_OUT_PROVIDER.getName());
            } else if (stockOutWareBillBo.oppositeType!=null && stockOutWareBillBo.oppositeType == BillTypeEnum.STOCK_OUT_WAREHOUSE.getKey()) {
                viewHolder.tvOppositeType.setText(BillTypeEnum.STOCK_OUT_WAREHOUSE.getName());
            }
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvBnsBillType)
        TextView tvBnsBillType;

        @BindView(R.id.tvBnsBillNo)
        TextView tvBnsBillNo;

        @BindView(R.id.tvOppositeType)
        TextView tvOppositeType;

        @BindView(R.id.tvOppositeName)
        TextView tvOppositeName;

        @BindView(R.id.ivBnsBillType)
        ImageView ivBnsBillType;

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

