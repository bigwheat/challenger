package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tqmall.R;
import com.tqmall.adapter.StockOutWareSearchAdapter;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.model.stockout.StockOutInfoDetailAgent;
import com.tqmall.mvp.model.stockout.StockOutWareBillBo;
import com.tqmall.mvp.presenter.impl.StockOutWarePresenterImpl;
import com.tqmall.mvp.view.StockOutWareView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.mvp.widget.ListRecyclerView;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.Logs;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/1/16.
 */

public class StockOutWareSearchActivity extends BaseActivity implements StockOutWareView,
        ListRecyclerView.OnRecyclerViewScrollBottomListener {


    @BindView(R.id.back)
    LinearLayout back;

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.mRecycleView)
    ListRecyclerView mRecycleView;

    @BindView(R.id.no_network)
    LinearLayout noNetWork;

    @BindView(R.id.no_data)
    LinearLayout noData;

    @BindView(R.id.show_data)
    LinearLayout showData;

    @BindView(R.id.no_data_network_image)
    ImageView noDataOrNetWork;


    @Inject
    StockOutWarePresenterImpl stockOutWarePresenter;

    @Inject
    StockOutWareSearchAdapter stockOutWareSearchAdapter;

    public ProgressDialog progressDialog;

    private int pageNumber = 1;

    private int pageSize = 10;

    //标识锁
    private boolean isLock = false;


    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_out_ware_search;
    }

    @Override
    protected void initData() {
        swipeRefresh.setColorSchemeResources(R.color.compat_holo_blue_bright, R.color.compat_holo_green_light,
                R.color.compat_holo_orange_light, R.color.compat_holo_red_light, R.color.burlywood);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                stockOutWarePresenter.queryStockOutInfo(pageNumber, pageSize, false);
            }
        });
        stockOutWarePresenter.queryStockOutInfo(pageNumber, pageSize, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initRecycleView(List<StockOutWareBillBo> data, boolean isLoadMore) {

        //首次加载的时候设置页面控件显示状态
        if (pageNumber == 1) {
            initViewStatus();
        }

        //初始化RecycleView
        if (data != null && data.size() > 0 && !isLoadMore) {
            if (stockOutWareSearchAdapter.getData() != null) {
                stockOutWareSearchAdapter.getData().clear();
                stockOutWareSearchAdapter.notifyDataSetChanged();
            }
            stockOutWareSearchAdapter.addDataList(data);
            //添加item点击事件
            stockOutWareSearchAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    StockOutWareBillBo data = stockOutWareSearchAdapter.getData().get(position);
                    if (data != null && data.id != null) {
                        stockOutWarePresenter.queryStockOutDetailInfo(data.id);
                    }
                }
            });

            //添加上拉加载更多,如果首次加载数据时 data的size小于分页的size则说明没有第二页数据,禁用上拉加载更多
            if (data.size() == pageSize && pageNumber == 1) {
                mRecycleView.setOnRecyclerViewScrollBottomListener(this);
            }
            mRecycleView.setAdapter(stockOutWareSearchAdapter);

            //如果为上拉加载更多,则隐藏footer,加载更多数据
        } else if (data != null && data.size() > 0 && isLoadMore) {
            stockOutWareSearchAdapter.hideFooter();
            stockOutWareSearchAdapter.addMore(data);
            //如果为初次加载数据的时候,返回的数据为空,那么则显示无Data的页面
        } else if ((data == null || data.size() == 0) && pageNumber == 1 && !isLoadMore) {
            noData();
        } else {
            stockOutWareSearchAdapter.hideFooter();
            CustomToast.showToastInBottom(mAct, "已无更多数据");
        }
    }

    /**
     * 初始化页面显示状态
     */
    private void initViewStatus() {
        showData.setVisibility(View.VISIBLE);
        noDataOrNetWork.setVisibility(View.GONE);
        noNetWork.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    @Override
    public void requestNextPage() {
        pageNumber++;
        stockOutWareSearchAdapter.showFooter();
        stockOutWarePresenter.queryStockOutInfo(pageNumber, pageSize, true);
        mRecycleView.scrollToPosition(stockOutWareSearchAdapter.getItemCount() - 1);
    }

    @Override
    public void returnStockInfoDetailData(List<StockOutInfoDetail> data) {
        if (data != null && data.size() > 0) {
            StockOutInfoDetailAgent stockOutInfoDetailAgent = new StockOutInfoDetailAgent();
            stockOutInfoDetailAgent.stockOutInfoDetailList = data;
//            IntentUtils.launchWithObjectNoFinish(mAct, StockOutWareDetailListActivity.class,
//                    Constant.INTENT_TAG_FROM_STOCK_OUT_SEARCH_ACTIVITY, stockOutInfoDetailAgent);

            Intent intent = new Intent(mAct, StockOutWareDetailListActivity.class);
            intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_SEARCH_ACTIVITY, stockOutInfoDetailAgent);
            startActivityForResult(intent, 0);

            Logs.d("TAG", data.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String str= String.valueOf( data.getExtras().get(Constant.INTENT_TAG_FROM_STOCK_OUT_SEARCH_ACTIVITY));
                if(!TextUtils.isEmpty(str)){
                    hideProgress();
                    hideRefresh();
                    stockOutWarePresenter.queryStockOutInfo(1, pageSize, false);
                }
            }
        }
    }

    @Override
    public void noData() {
        noData.setVisibility(View.VISIBLE);
        noDataOrNetWork.setVisibility(View.VISIBLE);
        showData.setVisibility(View.GONE);
        noNetWork.setVisibility(View.GONE);
    }

    @Override
    public void noNetWork() {
        noNetWork.setVisibility(View.VISIBLE);
        noDataOrNetWork.setVisibility(View.VISIBLE);
        showData.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    @OnClick({R.id.back, R.id.userCenter, R.id.retry})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;

            case R.id.retry:
                pageNumber = 1;
                if (!isLock) {
                    isLock = true;
                    stockOutWarePresenter.queryStockOutInfo(pageNumber, pageSize, false);
                    hideProgress();
                    isLock = false;
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockOutWarePresenter;
        mPresenter.attachView(this);
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
