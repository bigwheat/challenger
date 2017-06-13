package com.tqmall.mvp.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.StockOutWareDetailListAdapter;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.enmus.BillTypeEnum;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.model.stockout.StockOutInfoDetailAgent;
import com.tqmall.mvp.presenter.impl.StockOutWareDetailListPresenterImpl;
import com.tqmall.mvp.view.StockOutWareDetailListView;
import com.tqmall.mvp.widget.ListRecyclerView;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.Logs;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/1/17.
 */

public class StockOutWareDetailListActivity extends BaseActivity implements StockOutWareDetailListView,
        SwipeRefreshLayout.OnRefreshListener {


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

    @BindView(R.id.mStockOutRecycleView)
    ListRecyclerView mStockOutRecycleView;

    @BindView(R.id.tvStockOutStatus)
    TextView tvStockOutStatus;

    @BindView(R.id.no_data)
    LinearLayout no_data;

    @BindView(R.id.mLinearLayout)
    LinearLayout mLinearLayout;

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout swipeRefresh;

    @Inject
    StockOutWareDetailListAdapter stockOutWareDetailListAdapter;

    @Inject
    StockOutWareDetailListPresenterImpl stockOutWareDetailListPresenter;


    private StockOutInfoDetailAgent stockOutInfoDetailAgent;

    List<StockOutInfoDetail> stockOutInfoDetailList;

    public static final int TAG = 998;

    public static final int TAG_ONE=991;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_out_ware_detail_list;
    }

    @Override
    protected void initData() {
        swipeRefresh.setOnRefreshListener(this);
        judgeData();
    }

    private void judgeData() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        } else {
            try {
                stockOutInfoDetailAgent = (StockOutInfoDetailAgent) intent
                        .getExtras().get(Constant.INTENT_TAG_FROM_STOCK_OUT_SEARCH_ACTIVITY);
                if (stockOutInfoDetailAgent != null
                        && stockOutInfoDetailAgent.stockOutInfoDetailList != null
                        && stockOutInfoDetailAgent.stockOutInfoDetailList.size() > 0) {

                    stockOutInfoDetailList = stockOutInfoDetailAgent.stockOutInfoDetailList;
                    StockOutInfoDetail stockOutInfoDetail = stockOutInfoDetailList.get(0);
                    //初始化activity的头部信息
                    initHead(stockOutInfoDetail);
                    initRecycleView(stockOutInfoDetailList);
                }
            } catch (Exception e) {
                Logs.e("----->", e.toString());
            }
        }
    }

    /**
     * 初始化头部信息
     *
     * @param stockOutInfoDetail
     */
    private void initHead(StockOutInfoDetail stockOutInfoDetail) {

        if (stockOutInfoDetail != null) {
            if (stockOutInfoDetail.bnsBillType == BillTypeEnum.SALE_ORDER.getKey()) {
                tvBnsBillType.setText("销售单");
                ivBnsBillType.setImageResource(R.mipmap.iv_sale);
            } else if (stockOutInfoDetail.bnsBillType == BillTypeEnum.PURCHASE_RETURN_EXCHANGE_BILL.getKey()) {
                tvBnsBillType.setText("销售退货单");
                ivBnsBillType.setImageResource(R.mipmap.iv_return);
            }
            //单据号
            tvBnsBillNo.setText(stockOutInfoDetail.bnsBillNo + "");
            //对方名称
            tvOppositeName.setText(stockOutInfoDetail.oppositeName + "");
            //对方类型
            if (stockOutInfoDetail.oppositeType == BillTypeEnum.STOCK_OUT_CUSTOMER.getKey()) {
                tvOppositeType.setText(BillTypeEnum.STOCK_OUT_CUSTOMER.getName());
            } else if (stockOutInfoDetail.oppositeType == BillTypeEnum.STOCK_OUT_PROVIDER.getKey()) {
                tvOppositeType.setText(BillTypeEnum.STOCK_OUT_PROVIDER.getName());
            } else if (stockOutInfoDetail.oppositeType == BillTypeEnum.STOCK_OUT_WAREHOUSE.getKey()) {
                tvOppositeType.setText(BillTypeEnum.STOCK_OUT_WAREHOUSE.getName());
            }
        }
    }

    /**
     * 初始化RecycleView
     *
     * @param stockOutInfoDetailList
     */
    @Override
    public void initRecycleView(List<StockOutInfoDetail> stockOutInfoDetailList) {

        if (stockOutInfoDetailList != null && stockOutInfoDetailList.size() > 0) {
            if (stockOutWareDetailListAdapter != null && stockOutWareDetailListAdapter.getData() != null
                    && stockOutWareDetailListAdapter.getData().size() > 0) {
                stockOutWareDetailListAdapter.getData().clear();
                stockOutWareDetailListAdapter.notifyDataSetChanged();
            }
            stockOutWareDetailListAdapter.addDataList(stockOutInfoDetailList);

            stockOutWareDetailListAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    List<StockOutInfoDetail> stockOutInfoDetails = stockOutWareDetailListAdapter.getData();
                    if (stockOutInfoDetails != null
                            && stockOutInfoDetails.size() > 0) {
                        StockOutInfoDetailAgent stockOutInfoDetailAgent = new StockOutInfoDetailAgent();
                        stockOutInfoDetailAgent.stockOutInfoDetailList = stockOutInfoDetails;
                        stockOutInfoDetailAgent.position = position;
                        Intent intent = new Intent(mAct, StockOutWareGoodsDetailActivity.class);
                        intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_DETAIL_LIST_ACTIVITY, stockOutInfoDetailAgent);
                        startActivityForResult(intent, TAG);
                    }
                }
            });
            mStockOutRecycleView.setAdapter(stockOutWareDetailListAdapter);
        } else {
            mLinearLayout.setVisibility(View.GONE);
            tvStockOutStatus.setText("已拣完");
            tvStockOutStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            no_data.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        Integer id = stockOutInfoDetailList.get(0).id;
        if (null != id) {
            stockOutWareDetailListPresenter.queryStockOutDetailInfo(id);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent();
        intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_SEARCH_ACTIVITY,0);
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick({R.id.back, R.id.userCenter})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent =new Intent();
                intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_SEARCH_ACTIVITY,0);
                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;

            default:
                break;
        }
    }


    /**
     * 返回页面的时候传递发生改变的数据源并且重新刷新页面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG) {
            if (data != null && data.getExtras() != null) {

                StockOutInfoDetailAgent stockOutInfoDetailAgent = (StockOutInfoDetailAgent)
                        data.getExtras().get(Constant.INTENT_TAG_FROM_STOCK_OUT_DETAIL_LIST_ACTIVITY);

                if (stockOutInfoDetailAgent != null && stockOutInfoDetailAgent.stockOutInfoDetailList != null
                        && stockOutInfoDetailAgent.stockOutInfoDetailList.size() > 0) {

                    List<StockOutInfoDetail> stockOutInfoDetails = stockOutInfoDetailAgent.stockOutInfoDetailList;
                    if (stockOutWareDetailListAdapter.getData() != null && stockOutWareDetailListAdapter.getData().size() > 0) {
                        stockOutWareDetailListAdapter.getData().clear();
                        stockOutWareDetailListAdapter.notifyDataSetChanged();
                    }
                    stockOutWareDetailListAdapter.addDataList(stockOutInfoDetails);
                    mStockOutRecycleView.setAdapter(stockOutWareDetailListAdapter);
                } else {
                    mLinearLayout.setVisibility(View.GONE);
                    tvStockOutStatus.setText("已拣完");
                    tvStockOutStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                    no_data.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockOutWareDetailListPresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }

    @Override
    public void showToastMsg(String message, int status) {

    }


    @Override
    public void showRefresh() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

}
