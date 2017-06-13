package com.tqmall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.base.BasePagerAdapter;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.utils.Logs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Jay on 17/1/19.
 */

public class StockOutWareGoodsDetailPagerAdapter extends BasePagerAdapter<StockOutInfoDetail
        , StockOutWareGoodsDetailPagerAdapter.ViewHolder> {

    public List<StockOutInfoDetail> data;

    public PagerAdapterCallBack pagerAdapterCallBack;

    public int curPos;

    public StockOutWareGoodsDetailPagerAdapter(Context context, List<StockOutInfoDetail> data) {
        super(context, data);
    }

    public void setPagerAdapterCallBackListener(PagerAdapterCallBack pagerAdapterCallBack) {
        this.pagerAdapterCallBack = pagerAdapterCallBack;
    }

    public interface PagerAdapterCallBack {

        void setAllocationSn(List<StockOutInfoDetail> data, int position);

        void postData(List<StockOutInfoDetail> data, int position);

        void getScanAllocationSn(int position);
    }


    @Override
    protected ViewHolder viewHolder(View view, List<StockOutInfoDetail> data, int position) {
        return new ViewHolder(view, data, position);
    }

    @Override
    protected int setResId() {
        return R.layout.viewpager_stock_out_ware_goods_detail;
    }

    @Override
    protected void initPageView(ViewHolder holder, StockOutInfoDetail stockOutInfoDetail,
                                int position, List<StockOutInfoDetail> data) {

        this.data = data;
        holder.tvAdapterModels
                .setText(stockOutInfoDetail.adapterModels == null ? "" : stockOutInfoDetail.adapterModels);
        holder.tvGoodsFormat
                .setText(stockOutInfoDetail.goodsFormat == null ? "" : stockOutInfoDetail.goodsFormat);
        holder.tvGoodsName
                .setText(stockOutInfoDetail.goodsName == null ? "" : stockOutInfoDetail.goodsName);
        holder.tvGoodsSn
                .setText(stockOutInfoDetail.goodsSn == null ? "" : stockOutInfoDetail.goodsSn);
        holder.tvOeCode
                .setText(stockOutInfoDetail.oeCode == null ? "" : stockOutInfoDetail.oeCode);
        holder.tvOutQty
                .setText(stockOutInfoDetail.lockQty == null ? "" : (stockOutInfoDetail.lockQty + ""));
        holder.tvPrepareQty
                .setText(stockOutInfoDetail.releaseQty == null ? "" : (stockOutInfoDetail.releaseQty + ""));
        holder.tvVirtualAllocationSn
                .setText(stockOutInfoDetail.lockAllocationSn == null ? "" : stockOutInfoDetail.lockAllocationSn);
        holder.etAllocationSn
                .setText(stockOutInfoDetail.allocationSn == null ? (stockOutInfoDetail.lockAllocationSn) : (stockOutInfoDetail.allocationSn + ""));

        try {
            if (stockOutInfoDetail.lockQty != null && stockOutInfoDetail.releaseQty != null) {
                holder.etChangeQty.setHint("还剩" + (stockOutInfoDetail.lockQty - stockOutInfoDetail.releaseQty) + "件未出库");
                holder.etChangeQty.setText((stockOutInfoDetail.lockQty - stockOutInfoDetail.releaseQty)+"");
            }else if(stockOutInfoDetail.lockQty != null && stockOutInfoDetail.releaseQty == null){
                holder.etChangeQty.setHint("还剩" +stockOutInfoDetail.lockQty+ "件未出库");
                holder.etChangeQty.setText(stockOutInfoDetail.lockQty+"");
            }
        } catch (Exception e) {
            holder.etChangeQty.setHint("还剩" + stockOutInfoDetail.lockQty + "件未出库");
            holder.etChangeQty.setText(stockOutInfoDetail.lockQty+"");
        }


        try {
                int rate = stockOutInfoDetail.releaseQty * 100 / stockOutInfoDetail.lockQty;
                holder.tvRate.setText(rate + "%");
        } catch (Exception e) {
            holder.tvRate.setText("0%");
        }

        if (pagerAdapterCallBack != null) {
            pagerAdapterCallBack.setAllocationSn(data, position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    class ViewHolder {

        private StockOutInfoDetail dataPost;

        @BindView(R.id.tvGoodsSn)
        TextView tvGoodsSn;

        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;

        @BindView(R.id.tvOeCode)
        TextView tvOeCode;

        @BindView(R.id.tvAdapterModels)
        TextView tvAdapterModels;

        @BindView(R.id.tvGoodsFormat)
        TextView tvGoodsFormat;

        @BindView(R.id.tvVirtualAllocationSn)
        TextView tvVirtualAllocationSn;

        @BindView(R.id.tvOutQty)
        TextView tvOutQty;

        @BindView(R.id.tvPrepareQty)
        TextView tvPrepareQty;

        @BindView(R.id.etAllocationSn)
        EditText etAllocationSn;

        @BindView(R.id.etChangeQty)
        EditText etChangeQty;

        @BindView(R.id.tvCommit)
        TextView tvCommit;

        @BindView(R.id.tvRate)
        TextView tvRate;

        @OnTextChanged(value = {R.id.etAllocationSn}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
        void OnEtAllocationSnChanged() {
            String str = etAllocationSn.getText().toString().trim();
            if (!TextUtils.isEmpty(str)) {
                dataPost.allocationSn = str;
            } else {
                dataPost.allocationSn = "0";
            }
        }

        @OnTextChanged(value = {R.id.etChangeQty}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
        void OnEtStockOutChangeChanged() {
            try {
                String str = etChangeQty.getText().toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    dataPost.stockOutQty = Integer.valueOf(str);
                } else {
                    dataPost.stockOutQty = null;
                }
            } catch (Exception e) {
                Logs.e("error", e.toString());
                dataPost.stockOutQty = null;
            }
        }

        @OnClick({R.id.ivSearch, R.id.tvCommit})
        void OnClick(View view) {

            switch (view.getId()) {
                case R.id.ivSearch:
                    pagerAdapterCallBack.getScanAllocationSn(curPos);
                    break;

                case R.id.tvCommit:
                    if (dataPost.stockOutQty != null && !dataPost.allocationSn.equals("0")) {
                        data.set(curPos, dataPost);
                        pagerAdapterCallBack.postData(data, curPos);
                    }
                    break;

                default:
                    break;
            }
        }

        public ViewHolder(View view, List<StockOutInfoDetail> data, int position) {
            if (view != null) {
                ButterKnife.bind(this, view);
                dataPost = data.get(position);
            }
        }

    }
}
