package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tqmall.R;
import com.tqmall.adapter.StockInWareAdapter;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.enmus.BillTypeEnum;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.model.stockInware.StockInWareBillEntryBo;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.impl.StockInWarePresenterImpl;
import com.tqmall.mvp.view.StockInWareView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;

/**
 * Created by Jay on 16/12/12.
 */

public class StockInWareActivity extends BaseActivity implements StockInWareView, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {


    @Inject
    StockInWarePresenterImpl stockInWarePresenter;

    public ProgressDialog progressDialog;

    @BindView(R.id.content)
    LinearLayout linearLayout;

    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    @BindView(R.id.back)
    LinearLayout back;

    @BindView(R.id.userCenter)
    ImageView userCenter;

    @BindView(R.id.ivBnsBillType)
    ImageView ivBnsBillType;

    @BindView(R.id.tvBnsBillNo)
    TextView tvBnsBillNo;

    @BindView(R.id.tvOppositeName)
    TextView tvOppositeName;

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout swipeRefresh;

    public StockInWareAdapter stockInWareAdapter;

    public StockInWareBillBo mStockInWareBillBo;

    public List<StockInWareBillEntryBo> stockInWareBillEntryBoList;

    public String bnsBillNo;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_in_ware;
    }

    @Override
    protected void initData() {
        judgeData();
        setListener();

    }

    @Override
    public void onRefresh() {
        if(!TextUtils.isEmpty(bnsBillNo)){
            stockInWarePresenter.queryInWareBillEntryList(bnsBillNo, false);
        }
    }

    @Override
    public void initAllViews(final StockInWareBillBo stockInWareBillBo) {
        if (stockInWareBillBo.id == null) {
            showToastMsg("暂未查询到数据",CustomToast.WARN_STATUS);
            finish();
            return;
        }
        mStockInWareBillBo = stockInWareBillBo;
        tvBnsBillNo.setText(mStockInWareBillBo.bnsBillNo);
        tvOppositeName.setText(mStockInWareBillBo.oppositeName);
        if (mStockInWareBillBo.bnsBillType == BillTypeEnum.PURCHASE_BILL.getKey()) {
            ivBnsBillType.setImageResource(R.mipmap.iv_purchase_max);
        } else if (mStockInWareBillBo.bnsBillType == BillTypeEnum.ORDER_ADJUST_BILL.getKey()) {
            ivBnsBillType.setImageResource(R.mipmap.iv_adjust_max);
        } else if (mStockInWareBillBo.bnsBillType == BillTypeEnum.SALE_RETURN_EXCHANGE_BILL.getKey()) {
            ivBnsBillType.setImageResource(R.mipmap.iv_return_max);
        }
        stockInWareBillEntryBoList = mStockInWareBillBo.inWareBillEntryBoList;
        if (stockInWareBillEntryBoList != null && stockInWareBillEntryBoList.size() > 0) {
            stockInWareAdapter = new StockInWareAdapter();
            stockInWareAdapter.getData().addAll(stockInWareBillEntryBoList);
            mRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mRecycleView.setAdapter(stockInWareAdapter);
            stockInWareAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
//                    Logs.d("position and Data{}", stockInWareBillEntryBoList.get(position).toString());
                    StockInWareBillEntryBo stockInWareBillEntryBo = stockInWareBillEntryBoList.get(position);
                    //参数重新组装,为了与从搜索界面跳转到详情界面的BO保持一致
                    StockInWareInfo stockInWareInfo = new StockInWareInfo();
                    stockInWareInfo.warehouseBillId = mStockInWareBillBo.id;
                    stockInWareInfo.warehouseBillNo = mStockInWareBillBo.warehouseBillNo;
                    stockInWareInfo.bnsBillId = mStockInWareBillBo.bnsBillId;
                    stockInWareInfo.bnsBillNo = mStockInWareBillBo.bnsBillNo;
                    stockInWareInfo.bnsBillType = mStockInWareBillBo.bnsBillType;
                    stockInWareInfo.warehouseEntryId = stockInWareBillEntryBo.id;
                    stockInWareInfo.goodsId = stockInWareBillEntryBo.goodsId;
                    stockInWareInfo.goodsFormat = stockInWareBillEntryBo.goodsFormat;
                    stockInWareInfo.goodsSn = stockInWareBillEntryBo.goodsSn;
                    stockInWareInfo.goodsName = stockInWareBillEntryBo.goodsName;
                    stockInWareInfo.goodsUnit = stockInWareBillEntryBo.goodsUnit;
                    stockInWareInfo.adaptModels = stockInWareBillEntryBo.adaptModels;
                    stockInWareInfo.oeCode = stockInWareBillEntryBo.oeCode;
                    stockInWareInfo.inQty = stockInWareBillEntryBo.inQty;
                    stockInWareInfo.onShelvesQty = stockInWareBillEntryBo.onShelvesQty;
                    stockInWareInfo.batchNo = stockInWareBillEntryBo.batchNo;
                    stockInWareInfo.allocationSn = stockInWareBillEntryBo.allocationSn;
                    stockInWareInfo.virtualAllocationSn = stockInWareBillEntryBo.virtualAllocationSn;
                    //新增字段
                    stockInWareInfo.oppositeName=mStockInWareBillBo.oppositeName;
                    Intent intent = new Intent(mAct, StockInWareDetailActivity.class);
                    intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_IN, stockInWareInfo);
                    startActivity(intent);
                }
            });
        }
    }

    private void judgeData() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                bnsBillNo = intent.getExtras().getString(Constant.INTENT_TAG_FROM_STOCK_IN_DETAIL);
                stockInWarePresenter.queryInWareBillEntryList(bnsBillNo,true);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockInWarePresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;
            default:
                break;
        }
    }

    private void setListener() {
        back.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(this);
        userCenter.setOnClickListener(this);
    }

    @Override
    public void showProgress(String message) {
        progressDialog = Alert.progress(mAct, message, true);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMsg(String message) {
        hideProgress();
        Snackbar.make(linearLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMsg(String message, int status) {
        hideProgress();
        CustomToast.showToast(mAct, message, status);
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
