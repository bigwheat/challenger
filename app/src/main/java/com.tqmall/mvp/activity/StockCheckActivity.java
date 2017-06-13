package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.StockCheckWareListAdapter;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfoAgent;
import com.tqmall.mvp.model.stockcheck.StockCheckInfo;
import com.tqmall.mvp.presenter.impl.StockCheckPresenterImpl;
import com.tqmall.mvp.view.StockCheckView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.mvp.widget.ListRecyclerView;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/2/7.
 */

public class StockCheckActivity extends BaseActivity implements StockCheckView,
        ListRecyclerView.OnRecyclerViewScrollBottomListener, SwipeRefreshLayout.OnRefreshListener,
        BaseRecyclerListAdapter.OnRecyclerViewItemClickListener {


    @BindView(R.id.mRefresh)
    SwipeRefreshLayout mRefresh;

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
    StockCheckPresenterImpl stockCheckPresenter;

    @Inject
    StockCheckWareListAdapter stockCheckWareListAdapter;


    ProgressDialog progressDialog;

    private int pageNumber = 1;

    private int pageSize = 10;

    private boolean isLock = false;

    public static final int TAG = 0x12;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_check;
    }

    @Override
    protected void initData() {
        //添加监听
        setListener();
        stockCheckPresenter.queryStockCheckInfo(pageNumber, pageSize, false);
    }

    private void setListener() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(R.color.compat_holo_blue_bright, R.color.compat_holo_green_light,
                R.color.compat_holo_orange_light, R.color.compat_holo_red_light, R.color.burlywood);
        stockCheckWareListAdapter.setOnRecyclerViewItemClickListener(this);

    }

    @Override
    public void onRefresh() {
        pageNumber=1;
        stockCheckPresenter.queryStockCheckInfo(pageNumber, pageSize, false);
    }

    @Override
    public void requestNextPage() {
        pageNumber++;
        stockCheckWareListAdapter.showFooter();
        stockCheckPresenter.queryStockCheckInfo(pageNumber, pageSize, true);
        mRecycleView.scrollToPosition(stockCheckWareListAdapter.getItemCount() - 1);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        StockCheckInfo data = stockCheckWareListAdapter.getData().get(position);
        if (data != null && data.id != null) {
            //查询明细,是否存在数据,如果有那么就发送数据,默认为第一页的数据
            stockCheckPresenter.queryStockCheckDetailInfo(1, pageSize, data.id, null, null);
        }
    }

    @Override
    public void initRecycleView(List<StockCheckInfo> data, boolean isLoadMore) {

        if (pageNumber == 1) {
            initViewStatus();
        }
        if (data != null && data.size() > 0 && !isLoadMore) {
            if (stockCheckWareListAdapter.getData() != null) {
                stockCheckWareListAdapter.getData().clear();
                stockCheckWareListAdapter.notifyDataSetChanged();
            }
            stockCheckWareListAdapter.addDataList(data);

            //添加上拉加载更多,如果首次加载数据时 data的size小于分页的size则说明没有第二页数据,禁用上拉加载更多
            if (data.size() == pageSize && pageNumber == 1) {
                mRecycleView.setOnRecyclerViewScrollBottomListener(this);
            }
            mRecycleView.setAdapter(stockCheckWareListAdapter);

            //如果为上拉加载更多,则隐藏footer,加载更多数据
        } else if (data != null && data.size() > 0 && isLoadMore) {
            stockCheckWareListAdapter.hideFooter();
            stockCheckWareListAdapter.addMore(data);
            //如果为初次加载数据的时候,返回的数据为空,那么则显示无Data的页面
        } else if ((data == null || data.size() == 0) && pageNumber == 1 && !isLoadMore) {
            noData();
        } else {
            stockCheckWareListAdapter.hideFooter();
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
    public void returnStockInfoDetailData(List<StockCheckDetailInfo> data) {
        if (data != null && data.size() > 0) {
            StockCheckDetailInfoAgent stockCheckDetailInfoAgent = new StockCheckDetailInfoAgent();
            stockCheckDetailInfoAgent.stockCheckDetailInfos = data;
            Intent intent =new Intent(mAct,StockCheckDetailActivity.class);
            intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_CHECK_LIST_ACTIVITY, stockCheckDetailInfoAgent);
            startActivityForResult(intent,TAG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAG){
            if(resultCode == TAG){
                pageNumber=1;
                stockCheckPresenter.queryStockCheckInfo(pageNumber,pageSize,false);
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


    @OnClick({R.id.userCenter, R.id.back, R.id.retry})
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
                    stockCheckPresenter.queryStockCheckInfo(pageNumber, pageSize, false);
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
        mPresenter = stockCheckPresenter;
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
        mRefresh.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        if (mRefresh != null && mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(false);
        }
    }
}
