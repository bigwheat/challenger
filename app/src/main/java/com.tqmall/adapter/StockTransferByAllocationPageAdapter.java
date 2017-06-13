package com.tqmall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.base.BasePagerAdapter;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.utils.Logs;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Jay on 16/12/23.
 */

public class StockTransferByAllocationPageAdapter extends BasePagerAdapter<StockTransferWithAllocationSnBo
        , StockTransferByAllocationPageAdapter.ViewHolder> {

    public List<StockTransferWithAllocationSnBo> data;

    public int curPos;

    public AdapterDataCallBack adapterDataCallBack;

    public StockTransferByAllocationPageAdapter(Context context, List<StockTransferWithAllocationSnBo> data) {
        super(context, data);
    }

    @Override
    protected ViewHolder viewHolder(View view, List<StockTransferWithAllocationSnBo> data, int position) {
        return new ViewHolder(view, data, position);
    }

    @Override
    protected int setResId() {
        return R.layout.viewpager_stock_transfer_by_allocationsn_new;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    protected void initPageView(StockTransferByAllocationPageAdapter.ViewHolder holder,
                                StockTransferWithAllocationSnBo stockTransferWithAllocationSnBo,
                                int position, List<StockTransferWithAllocationSnBo> data) {
        this.data = data;
        holder.tvAdapterModels
                .setText(stockTransferWithAllocationSnBo.adapterModels == null ? "" : stockTransferWithAllocationSnBo.adapterModels);
        holder.tvGoodsFormat
                .setText(stockTransferWithAllocationSnBo.goodsFormat == null ? "" : stockTransferWithAllocationSnBo.goodsFormat);
        holder.tvGoodsName
                .setText(stockTransferWithAllocationSnBo.goodsName == null ? "" : stockTransferWithAllocationSnBo.goodsName);
        holder.tvGoodsSn
                .setText(stockTransferWithAllocationSnBo.goodsSn == null ? "" : stockTransferWithAllocationSnBo.goodsSn);
        holder.tvGoodsUnit
                .setText((stockTransferWithAllocationSnBo.instoreQty == null ? "" : stockTransferWithAllocationSnBo.instoreQty)
                        +"/"+(stockTransferWithAllocationSnBo.goodsUnit == null ? "" : stockTransferWithAllocationSnBo.goodsUnit));

        if(stockTransferWithAllocationSnBo.destAllocationSn!=null){
            holder.etAllocationSn.setText(stockTransferWithAllocationSnBo.destAllocationSn);
            holder.etAllocationSn.setSelectAllOnFocus(true);
            holder.etAllocationSn.selectAll();
        }
        if(stockTransferWithAllocationSnBo.changeQty!=null){
            holder.etChangeQty.setText(stockTransferWithAllocationSnBo.changeQty+"");
        }
        if (adapterDataCallBack != null) {
            adapterDataCallBack.setAllocationSn(data, position);
        }
    }

    public void setAdapterDataCallBackListener(AdapterDataCallBack adapterDataCallBack) {
        this.adapterDataCallBack = adapterDataCallBack;
    }

    public interface AdapterDataCallBack {

        void setAllocationSn(List<StockTransferWithAllocationSnBo> data, int position);

        void postData(List<StockTransferWithAllocationSnBo> data, int position);

        void getDestAllocationSn(int position);
    }

    class ViewHolder {

        private StockTransferWithAllocationSnBo dataPost;

        @BindView(R.id.tvGoodsSn)
        TextView tvGoodsSn;

        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;

        @BindView(R.id.tvAdapterModels)
        TextView tvAdapterModels;

        @BindView(R.id.tvGoodsFormat)
        TextView tvGoodsFormat;

        @BindView(R.id.tvGoodsUnit)
        TextView tvGoodsUnit;

        @BindView(R.id.etAllocationSn)
        EditText etAllocationSn;

        @BindView(R.id.etChangeQty)
        EditText etChangeQty;

        public ViewHolder(View view, List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList, int position) {
            if (view != null) {
                ButterKnife.bind(this, view);
                dataPost = stockTransferWithAllocationSnBoList.get(position);
            }
        }

        @OnTextChanged(value = {R.id.etAllocationSn}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
        void OnEtAllocationSnChanged() {
            String str = etAllocationSn.getText().toString().trim();
            if (!TextUtils.isEmpty(str)) {
                dataPost.destAllocationSn = str;
            } else {
                dataPost.destAllocationSn = "0";
            }
        }

        @OnTextChanged(value = {R.id.etChangeQty}, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
        void OnEtChangeQtyChanged() {
            try {
                String str = etChangeQty.getText().toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    dataPost.changeQty = Integer.valueOf(str);
                } else {
                    dataPost.changeQty = null;
                }
            } catch (Exception e) {
                Logs.e("error", e.toString());
                dataPost.changeQty = null;
            }
        }

        @OnClick({R.id.ivSearch, R.id.tvCommit})
        void OnClick(View view) {
            switch (view.getId()) {
                case R.id.ivSearch:
                    adapterDataCallBack.getDestAllocationSn(curPos);
                    break;
                case R.id.tvCommit:
                    if (dataPost.changeQty != null&&!dataPost.destAllocationSn.equals("0")) {
                        data.set(curPos, dataPost);
                        adapterDataCallBack.postData(data, curPos);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
