package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.StockCheckWareDetailAdapter;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfoAgent;
import com.tqmall.mvp.presenter.impl.StockCheckDetailPresenterImpl;
import com.tqmall.mvp.view.StockCheckDetailView;
import com.tqmall.mvp.widget.CustomDialog;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.mvp.widget.ListRecyclerView;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.SignUtil;
import com.tqmall.utils.UmengUtils;
import com.tqmall.utils.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/2/8.
 */

public class StockCheckDetailActivity extends BaseActivity implements StockCheckDetailView,
        ListRecyclerView.OnRecyclerViewScrollBottomListener, SwipeRefreshLayout.OnRefreshListener,
        StockCheckWareDetailAdapter.StockCheckWareDetailAdapterCallBack {


    @BindView(R.id.tvRate)
    TextView tvRate;

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout mRefresh;

    @BindView(R.id.tvWarehouseBillNo)
    TextView tvWarehouseBillNo;

    @BindView(R.id.mRecycleView)
    ListRecyclerView mRecycleView;

    @BindView(R.id.no_network)
    LinearLayout noNetWork;

    @BindView(R.id.no_data)
    LinearLayout noData;

    @BindView(R.id.show_data)
    LinearLayout showData;

    @BindView(R.id.ivScan)
    ImageView ivScan;

    @BindView(R.id.ivScanGoods)
    ImageView ivScanGoods;

    @BindView(R.id.etAllocationSn)
    EditText etAllocationSn;

    @BindView(R.id.etGoodsSn)
    EditText etBatchNo;

    @BindView(R.id.ivClearAllocationSn)
    ImageView ivClearAllocationSn;

    @BindView(R.id.ivClearBatchNo)
    ImageView ivClearBatchNo;

    @Inject
    StockCheckDetailPresenterImpl stockCheckDetailPresenter;

    @Inject
    StockCheckWareDetailAdapter stockCheckWareDetailAdapter;

    ProgressDialog progressDialog;

    StockCheckDetailInfoAgent stockCheckDetailInfoAgent;

    List<StockCheckDetailInfo> stockCheckDetailInfo;

    private int pageNumber = 1;

    private int pageSize = 10;

    private boolean isSearched = false;

    public static final int TAG_INTENT = 11;

    public String allocationSnStr;

    public static final String SCAN_ALLOCATION_SN = "SCAN_ALLOCATION_SN";

    public static final String SCAN_BATCH_NO = "SCAN_BATCH_NO";


    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_check_detail;
    }

    @Override
    protected void initData() {
        setListener();
        judgeData();
    }

    private void setListener() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(R.color.compat_holo_blue_bright, R.color.compat_holo_green_light,
                R.color.compat_holo_orange_light, R.color.compat_holo_red_light, R.color.burlywood);
        stockCheckWareDetailAdapter.setStockCheckWareDetailAdapterCallBack(this);

        etAllocationSn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                    allocationSnStr = etAllocationSn.getText().toString().trim();
                    if (!TextUtils.isEmpty(allocationSnStr)) {
                        String batchNoStr = etBatchNo.getText().toString().trim();
                        pageNumber=1;
                        isSearched = true;
                        if (!TextUtils.isEmpty(batchNoStr)) {
                            stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                                    warehouseBillId, allocationSnStr, batchNoStr, false);
                        } else {
                            stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                                    warehouseBillId, allocationSnStr, null, false);
                        }
                    } else {
                        showToastMsg("请先输入或扫描库位码", CustomToast.WARN_STATUS);
                    }
                    return true;
                }
                return false;
            }
        });


        etBatchNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                    String batchNoStr = etBatchNo.getText().toString().trim();
                    String allocationSnStr1 = etAllocationSn.getText().toString().trim();
                    if (!TextUtils.isEmpty(allocationSnStr1)) {
                        if (!TextUtils.isEmpty(batchNoStr)) {
                            stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                                    warehouseBillId, allocationSnStr1, batchNoStr, false);
                        } else {
                            stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                                    warehouseBillId, allocationSnStr1, null, false);
                        }
                    } else {
                        showToastMsg("请先输入或扫描库位码", CustomToast.WARN_STATUS);
                    }
                    return true;
                }
                return false;
            }
        });

        etAllocationSn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    ivClearAllocationSn.setVisibility(View.VISIBLE);
                    ivScanGoods.setImageResource(R.mipmap.stock_check_scan_goods_red);
                    etBatchNo.setVisibility(View.VISIBLE);
                } else {
                    ivClearAllocationSn.setVisibility(View.INVISIBLE);
                    ivScanGoods.setImageResource(R.mipmap.stock_check_scan_goods_dark);
                    etBatchNo.setVisibility(View.INVISIBLE);
                    etBatchNo.setText("");
                    ivClearBatchNo.setVisibility(View.INVISIBLE);
                }
            }
        });


        etBatchNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    ivClearBatchNo.setVisibility(View.VISIBLE);
                } else {
                    ivClearBatchNo.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void judgeData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        } else {
            try {
                stockCheckDetailInfoAgent = (StockCheckDetailInfoAgent)
                        intent.getExtras().get(Constant.INTENT_TAG_FROM_STOCK_CHECK_LIST_ACTIVITY);
                stockCheckDetailInfo = stockCheckDetailInfoAgent.stockCheckDetailInfos;
                if (stockCheckDetailInfoAgent != null && stockCheckDetailInfo != null
                        && stockCheckDetailInfo.size() > 0) {
                    //初始化页面
                    initHead(stockCheckDetailInfo);
                    initRecycleView(stockCheckDetailInfo, false);
                }

            } catch (Exception e) {

            }
        }
    }

    private void initHead(List<StockCheckDetailInfo> stockCheckDetailInfo) {

        StockCheckDetailInfo stockCheckDetailInfos = stockCheckDetailInfo.get(0);
        if (stockCheckDetailInfos != null) {
            tvWarehouseBillNo.setText(stockCheckDetailInfos.warehouseBillNo == null ? "" : stockCheckDetailInfos.warehouseBillNo);
            tvRate.setText("已操作 " + stockCheckDetailInfos.checkedEntriesCount + "/" + stockCheckDetailInfos.entriesCount);
        }

    }

    @Override
    public void initRecycleView(List<StockCheckDetailInfo> data, boolean isLoadMore) {
        if (pageNumber == 1) {
            initViewStatus();
        }

        if (data != null && data.size() > 0 && !isLoadMore) {
            tvRate.setText("已操作 " + data.get(0).checkedEntriesCount + "/" + data.get(0).entriesCount);
            if (stockCheckWareDetailAdapter.getData() != null) {
                stockCheckWareDetailAdapter.getData().clear();
                stockCheckWareDetailAdapter.notifyDataSetChanged();
            }
            stockCheckWareDetailAdapter.addDataList(data);

            //添加上拉加载更多,如果首次加载数据时 data的size小于分页的size则说明没有第二页数据,禁用上拉加载更多
            if (data.size() == pageSize && pageNumber == 1) {
                mRecycleView.setOnRecyclerViewScrollBottomListener(this);
            }
            //为搜索的情况下且不为上拉加载更多时禁用上拉加载更多
            if(isSearched){
                if(data.size() != pageSize && pageNumber==1){
                    mRecycleView.setLoadBottomDataCompleted(false);
                    stockCheckWareDetailAdapter.hideFooter();
                }
            }

            mRecycleView.setAdapter(stockCheckWareDetailAdapter);

            //如果为上拉加载更多,则隐藏footer,加载更多数据
        } else if (data != null && data.size() > 0 && isLoadMore) {
            stockCheckWareDetailAdapter.hideFooter();
            stockCheckWareDetailAdapter.addMore(data);
            //如果为初次加载数据的时候,返回的数据为空,那么则显示无Data的页面
        } else if ((data == null || data.size() == 0) && pageNumber == 1 && !isLoadMore) {
            noData();
            tvRate.setText("已操作 0/0");
        } else {
            stockCheckWareDetailAdapter.hideFooter();
            CustomToast.showToastInBottom(mAct, "已无更多数据");
        }
    }

    /**
     * 通过提示框提交盘点数据后重新刷新也面,并滑动到原始位置
     *
     * @param originData
     * @param originPos
     * @param newData
     */
    @Override
    public void initRecycleViewAfterPostData(List<StockCheckDetailInfo> originData, int originPos,
                                             StockCheckDetailInfo newData, List<StockCheckDetailInfo> noChangeData, boolean isSuccess) {
        if (newData != null) {
            if (newData.entriesCount != null && newData.checkedEntriesCount != null) {
                tvRate.setText("已操作 " + newData.checkedEntriesCount + "/" + newData.entriesCount);
            }
        }
        if (isSuccess) {
            List<StockCheckDetailInfo> stockCheckDetailInfo = stockCheckWareDetailAdapter.getData();
            stockCheckDetailInfo.get(originPos).checkAfterQty = newData.checkAfterQty;
        }
        mRecycleView.setAdapter(stockCheckWareDetailAdapter);
        stockCheckWareDetailAdapter.notifyDataSetChanged();
        mRecycleView.scrollToPosition(originPos);
    }

    @Override
    public void onRefresh() {
        pageNumber = 1;
        if(!isSearched){
            if (stockCheckDetailInfo != null && stockCheckDetailInfo.get(0).id != null) {
                isSearched=false;
                stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).warehouseBillId, null, null, false);
            }
        }else {
            String str=etAllocationSn.getText().toString().trim();
            if(!TextUtils.isEmpty(str)) {
                stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                        warehouseBillId, str, null, false);
            }
        }
    }

    @Override
    public void setOnItemDetailClick(View view, List<StockCheckDetailInfo> data, int position) {
        if (data != null && data.get(position) != null) {
            Intent intent = new Intent(mAct, StockCheckGoodsDetailActivity.class);
            StockCheckDetailInfoAgent stockCheckDetailInfoAgent = new StockCheckDetailInfoAgent();
            stockCheckDetailInfoAgent.stockCheckDetailInfos = data;
            stockCheckDetailInfoAgent.position = position;
            intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_CHECK_DETAIL_ACTIVITY, stockCheckDetailInfoAgent);
            startActivityForResult(intent, TAG_INTENT);
        }
    }

    @Override
    public void setOnItemPostDataClick(View view, final List<StockCheckDetailInfo> data, final int position) {

        if (data != null && data.get(position) != null) {
            final StockCheckDetailInfo stockCheckDetailInfo = data.get(position);
            new CustomDialog.Builder(this).setTitle(stockCheckDetailInfo.goodsName + "")
                    .setMessage(stockCheckDetailInfo.checkAfterQty)
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton("确定", new CustomDialog.Builder.PriorityListener() {

                        @Override
                        public void negativeButtonClickListener(DialogInterface dialog, int which, String changeQty) {
                            if (!TextUtils.isEmpty(changeQty)) {
                                try {
                                    Integer checkedQty = Integer.valueOf(changeQty);
                                    if (checkedQty != null) {
                                        List<StockCheckDetailInfo> stockCheckDetailInfoList = SignUtil.deepCopy(data);
                                        stockCheckDetailInfoList.get(position).checkAfterQty = checkedQty;
                                        stockCheckDetailInfoList.get(position).allocationSn=etAllocationSn.getText().toString().trim();
                                        stockCheckDetailPresenter.postStockCheckData(stockCheckDetailInfoList, position, data);
                                    }else {
                                        showToastMsg("盘点数量不能为空", CustomToast.WARN_STATUS);
                                    }
                                } catch (Exception e) {
                                    UmengUtils.reportError(mAct, "StockCheckDetailActivity.setOnItemPostDataClick()",
                                            e.toString() == null ? "" : e.toString());
                                    showToastMsg("输入数据有误,请重新输入", CustomToast.WARN_STATUS);
                                }
                            } else {
                                showToastMsg("盘点数量不能为空", CustomToast.WARN_STATUS);
                            }
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }

    @Override
    public void requestNextPage() {
        //如果是未通过搜索
        if(!isSearched){
            if (stockCheckDetailInfo != null && stockCheckDetailInfo.get(0).warehouseBillId != null) {
                pageNumber++;
                stockCheckWareDetailAdapter.showFooter();
                stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).warehouseBillId, null, null, true);
                mRecycleView.scrollToPosition(stockCheckWareDetailAdapter.getItemCount()-1);
            }
        //通过搜索
        }else {
            String str=etAllocationSn.getText().toString().trim();

            if(!TextUtils.isEmpty(str)){
               pageNumber++;
               stockCheckWareDetailAdapter.showFooter();
               stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                       warehouseBillId, str, null, false);
               mRecycleView.scrollToPosition(stockCheckWareDetailAdapter.getItemCount()-1);

           }else {
               showToastMsg("库位码不能为空",CustomToast.WARN_STATUS);
           }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent();
        setResult(StockCheckActivity.TAG,intent);
        finish();
    }

    @OnClick({R.id.userCenter, R.id.back, R.id.retry, R.id.ivScan, R.id.ivScanGoods,R.id.ivClearAllocationSn,R.id.ivClearBatchNo})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent0 =new Intent();
                setResult(StockCheckActivity.TAG,intent0);
                finish();
                break;
            case R.id.ivClearAllocationSn:
                etAllocationSn.setText("");
                break;
            case R.id.ivClearBatchNo:
                etBatchNo.setText("");
                break;
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;
            case R.id.retry:
                stockCheckDetailPresenter.queryStockCheckDetailInfo(1, pageSize, stockCheckDetailInfo.get(0).warehouseBillId, null, null, false);
                hideProgress();
                break;
            case R.id.ivScan:
                Intent intentAllSn = new Intent(mAct, CaptureActivity.class);
                startActivityForResult(intentAllSn, 56);
                break;

            case R.id.ivScanGoods:
                if (!TextUtils.isEmpty(etAllocationSn.getText().toString())) {
                    Intent intent = new Intent(mAct, CaptureActivity.class);
                    intent.putExtra(SCAN_BATCH_NO, SCAN_BATCH_NO);
                    startActivityForResult(intent, 57);
                } else {
                    showToastMsg("请先输入或扫描库位码", CustomToast.WARN_STATUS);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_INTENT) {
            if (data != null && data.getExtras() != null) {
                StockCheckDetailInfoAgent stockCheckDetailInfoAgent =
                        (StockCheckDetailInfoAgent) data.getExtras().get(Constant.INTENT_TAG_FROM_STOCK_CHECK_DETAIL_ACTIVITY);
                if (stockCheckDetailInfoAgent != null && stockCheckDetailInfoAgent.stockCheckDetailInfos != null
                        && stockCheckDetailInfoAgent.position != null && stockCheckDetailInfoAgent.stockCheckDetailInfos.size() > 0) {
                    List<StockCheckDetailInfo> stockCheckDetailInfoList = stockCheckWareDetailAdapter.getData();
                    stockCheckDetailInfoList.get(stockCheckDetailInfoAgent.position).checkAfterQty =
                            stockCheckDetailInfoAgent.stockCheckDetailInfos.get(stockCheckDetailInfoAgent.position).checkAfterQty;
                    mRecycleView.setAdapter(stockCheckWareDetailAdapter);
                    stockCheckWareDetailAdapter.notifyDataSetChanged();
                    mRecycleView.scrollToPosition(stockCheckDetailInfoAgent.position);
                }
            }
        }
        //扫码
        if (resultCode == RESULT_OK) {
            //扫码的库位码
            if(requestCode == 56){
                if(data != null && data.getExtras() != null){
                    String scanResultAllocationSn = data.getExtras().getString("result");
                    if (!TextUtils.isEmpty(scanResultAllocationSn)) {
                        etAllocationSn.setText(scanResultAllocationSn);
                        allocationSnStr=scanResultAllocationSn;
                        pageNumber = 1;
                        isSearched=true;
                        stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                                warehouseBillId, scanResultAllocationSn, null, false);
                    }
                }
            }

            if(requestCode == 57){
                if(data != null && data.getExtras() != null){
                    String scanAllocationStr =etAllocationSn.getText().toString().trim();
                    if(!TextUtils.isEmpty(scanAllocationStr)){
                        String batchNoStr = data.getExtras().getString("result");
                        allocationSnStr=scanAllocationStr;
                        if(!TextUtils.isEmpty(batchNoStr)){
                            etBatchNo.setText(batchNoStr);
                            pageNumber = 1;
                            isSearched=true;
                            stockCheckDetailPresenter.queryStockCheckDetailInfo(pageNumber, pageSize, stockCheckDetailInfo.get(0).
                                    warehouseBillId, scanAllocationStr, batchNoStr, false);
                        }

                    }else {
                        showToastMsg("请先输入或扫描库位码",CustomToast.WARN_STATUS);
                    }
                }
            }
        }
    }

    /**
     * 初始化页面显示状态
     */
    private void initViewStatus() {
        showData.setVisibility(View.VISIBLE);
        noNetWork.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    @Override
    public void noData() {
        noData.setVisibility(View.VISIBLE);
        showData.setVisibility(View.GONE);
        noNetWork.setVisibility(View.GONE);
    }

    @Override
    public void noNetWork() {
        noNetWork.setVisibility(View.VISIBLE);
        showData.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockCheckDetailPresenter;
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
