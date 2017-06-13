package com.tqmall.mvp.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tqmall.R;
import com.tqmall.adapter.StockInWareSearchAdapter;
import com.tqmall.adapter.base.BaseRecyclerListAdapter;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.impl.StockInWareSearchPresenterImpl;
import com.tqmall.mvp.view.StockInWareSearchView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.zxing.activity.CaptureActivity;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 16/12/14.
 */

public class StockInWareSearchActivity extends BaseActivity implements StockInWareSearchView {



    @BindView(R.id.etSearch)
    EditText mSearch;

    @BindView(R.id.show_data)
    LinearLayout hasData;

    @BindView(R.id.no_network)
    LinearLayout noNetwork;

    @BindView(R.id.no_data)
    LinearLayout noData;

    @BindView(R.id.tvSearch)
    TextView tvSearch;

    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    @BindView(R.id.content)
    LinearLayout linearLayout;

    @BindView(R.id.no_data_network)
    ImageView show_line;

    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.search)
    LinearLayout search;

    private StockInWareSearchAdapter stockInWareSearchAdapter;

    private ProgressDialog progressDialog;

    public final static int INTENT_FLAG = 99;

    public final static int INTENT_FLAG_PART = 98;


    /**
     * 刷新的页面pos
     */
    public int refreshPos = 0;

    public int pageNumber = 1;

    public int pageSize = 10;

    private boolean isLoadMore = false;

    private float x1, x2, y1, y2;

    @Inject
    StockInWareSearchPresenterImpl stockInWareSearchPresenter;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_in_ware_search;
    }

    @OnClick(R.id.search)
    void tvSearch() {
        Intent intent = new Intent(mAct, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.userCenter)
    void home() {
        IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
    }

    @OnClick(R.id.tvConfirm)
    void confirm() {
        String scanResult = mSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(scanResult)) {
            stockInWareSearchPresenter.initStockInWareData(scanResult);
        }
    }

    @OnClick({R.id.activity_search_top})
    void jumpTpSearch(View view) {
        showShareAnimation(view);
    }

    @OnClick(R.id.no_network)
    void setNoNetwork() {
        noNetwork.setVisibility(View.GONE);
        hasData.setVisibility(View.VISIBLE);
        stockInWareSearchPresenter.initRecycleViewData(pageSize, 1, false);
    }

    @Override
    protected void initData() {
        stockInWareSearchAdapter = new StockInWareSearchAdapter();
        swipeRefresh.setColorSchemeResources(R.color.compat_holo_blue_bright, R.color.compat_holo_green_light,
                R.color.compat_holo_orange_light, R.color.compat_holo_red_light, R.color.burlywood);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                stockInWareSearchPresenter.initRecycleViewData(pageSize, 1, isLoadMore);
                pageNumber = 1;
            }
        });
        stockInWareSearchPresenter.initRecycleViewData(pageSize, pageNumber, false);
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockInWareSearchPresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void noNetWork() {
        //没网显示下划线阴影效果,RecycleView隐藏,无网络连接的页面显示
        hasData.setVisibility(View.GONE);
        show_line.setVisibility(View.VISIBLE);
        noNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void noData() {
        //没入库的数据显示下划线阴影效果,RecycleView隐藏,无数据的页面显示
        noNetwork.setVisibility(View.GONE);
        show_line.setVisibility(View.VISIBLE);
        hasData.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMoreData(final List<StockInWareInfo> stockInWareInfo, boolean isNeedLoadMore) {

        if (stockInWareInfo.size() < pageSize && pageNumber == 1) {
            return;
        }

        //监听上拉加载更多事件
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                //如果第一页加载的数据小于分页的数目,则禁用上拉加载更多


                if (!isLoadMore && visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition >= totalItemCount - 1) {

                    isLoadMore = true;
                    pageNumber++;
                    stockInWareSearchAdapter.showFooter();
                    stockInWareSearchPresenter.initRecycleViewData(pageSize, pageNumber, isLoadMore);
                    mRecycleView.scrollToPosition(stockInWareSearchAdapter.getItemCount() - 1);
                    isLoadMore = false;
                }
            }
        });
    }

    /**
     * 加载RecycleView数据
     * isNeedLoadMore 为false表示为不需要加载更多
     * 它会做2件事:
     * 1.在初次进入页面时,RecycleView会自动初始化数据
     * 2.在下拉加载更多时候显示foot 在数据源中插入数据并更新RecycleView
     *
     * @param stockInWareInfo
     * @param isNeedLoadMore
     */
    @Override
    public void initRecycle(final List<StockInWareInfo> stockInWareInfo, boolean isNeedLoadMore) {


        hideProgress();
        //首次刷新页面的时候需要重置控件属性
        if (pageNumber == 1) {
            noNetwork.setVisibility(View.GONE);
            show_line.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        }

        hasData.setVisibility(View.VISIBLE);
        //初始化RecycleView
        if (stockInWareInfo != null && stockInWareInfo.size() > 0 && !isNeedLoadMore) {
            if (stockInWareSearchAdapter != null && stockInWareSearchAdapter.getData() != null) {
                stockInWareSearchAdapter.getData().clear();
                stockInWareSearchAdapter.notifyDataSetChanged();
            }
            stockInWareSearchAdapter.addDataList(stockInWareInfo);
            //添加上拉加载更多
            loadMoreData(stockInWareInfo, !isNeedLoadMore);
            stockInWareSearchAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    StockInWareInfo stockInWareInfo1 = stockInWareSearchAdapter.getData().get(position);
                    refreshPos = position;
//                    Logs.d("stockInWareSearchAdapter.getData().toString():{}", stockInWareSearchAdapter.getData().toString());
                    Intent intent = new Intent(mAct, StockInWareDetailActivity.class);
                    intent.putExtra(Constant.INTENT_TAG_FROM_SEARCH_WITH_LIST, stockInWareInfo1);
                    startActivityForResult(intent, INTENT_FLAG);
                }
            });

            mRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mRecycleView.setHasFixedSize(true);
            //添加加载动画效果
            mRecycleView.setItemAnimator(new DefaultItemAnimator());

            mRecycleView.setAdapter(stockInWareSearchAdapter);

        } else if (stockInWareInfo != null && stockInWareInfo.size() > 0 && isNeedLoadMore) {
            stockInWareSearchAdapter.hideFooter();
            stockInWareSearchAdapter.addMore(stockInWareInfo);
        } else {
            stockInWareSearchAdapter.hideFooter();
            CustomToast.showToastInBottom(mAct, "已无更多数据");
        }

    }

    @Override
    public void returnData(StockInWareInfo stockInWareInfo) {
        if (stockInWareInfo != null) {
            IntentUtils.launchWithObjectNoFinish(mAct, StockInWareDetailActivity.class
                    , Constant.INTENT_TAG_FROM_SEARCH_WITH_BATCH, stockInWareInfo);

        }else {
            showToastMsg("未查到该批次码对应的商品信息",CustomToast.WARN_STATUS);
        }
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 获取到共享元素的位置信息，并将其传递给下一个界面
     * getGlobalVisibleRect() 方法的含义是，
     * 获取 可见的状态栏高度+可见的标题栏高度+Rect左上角到标题栏底部的距离，
     * 如果标题栏被隐藏了，那么可见标题栏高度为0
     *
     * @param view
     */
    private void showShareAnimation(View view) {
        IntentUtils.launchNoFinish(mAct,SearchActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                mSearch.setText(scanResult);
                //扫码成功后直接跳转
                stockInWareSearchPresenter.initStockInWareData(scanResult);
            }
        }

        if (requestCode == INTENT_FLAG) {
            String str=null;
            StockInWareInfo stockInWareInfo=null;
            if(data!=null && data.getExtras()!=null){
                str = (String) data.getExtras().get(String.valueOf(INTENT_FLAG));
                stockInWareInfo = (StockInWareInfo) data.getExtras().get(String.valueOf(INTENT_FLAG_PART));
            }

            //入库完成一种商品后,从数据源中移除该item,刷新
            if (str != null && str.equals(StockInWareDetailActivity.STOCK_IN_ALL)) {
                List<StockInWareInfo> dataList=stockInWareSearchAdapter.getData();
                if(dataList!=null){
                    dataList.remove(refreshPos);
                    stockInWareSearchAdapter.notifyDataSetChanged();
                    if(refreshPos!=0){
                        mRecycleView.scrollToPosition(refreshPos-1);
                    }else {
                        mRecycleView.scrollToPosition(refreshPos);
                    }
                }
            }

            //部分入库完成一种商品后,替换该item,刷新
            if (stockInWareInfo != null) {
                List<StockInWareInfo> dataList=stockInWareSearchAdapter.getData();
                if(dataList!=null){
                    dataList.set(refreshPos,stockInWareInfo);
                    stockInWareSearchAdapter.notifyDataSetChanged();
                    mRecycleView.scrollToPosition(refreshPos);
                }

            }
        }



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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            //当手指按下的时候
//             x1 = event.getX();
//             y1 = event.getY();
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            if(y1 - y2 > 50) {
//                Toast.makeText(mAct, "向上滑", Toast.LENGTH_SHORT).show();
//            } else if(y2 - y1 > 50) {
//                Toast.makeText(mAct, "向下滑", Toast.LENGTH_SHORT).show();
//            } else if(x1 - x2 > 50) {
//                Toast.makeText(mAct, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(mAct, "向右滑", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return super.onTouchEvent(event);
//    }
}
