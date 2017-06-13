package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.StockOutWareGoodsDetailPagerAdapter;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockout.StockOutInfoDetail;
import com.tqmall.mvp.model.stockout.StockOutInfoDetailAgent;
import com.tqmall.mvp.presenter.impl.StockOutWareGoodsDetailPresenterImpl;
import com.tqmall.mvp.view.StockOutWareGoodsDetailView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.mvp.widget.ViewPagerTransformer;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/1/18.
 */

public class StockOutWareGoodsDetailActivity extends BaseActivity implements StockOutWareGoodsDetailView,
        ViewPager.OnPageChangeListener, StockOutWareGoodsDetailPagerAdapter.PagerAdapterCallBack {

    @BindView(R.id.tvBnsBillNo)
    TextView tvBnsBillNo;

    @BindView(R.id.mStockOutViewPager)
    ViewPager mStockOutViewPager;

    @BindView(R.id.tvCount)
    TextView tvCount;

    @BindView(R.id.previousPage)
    TextView previousPage;

    @BindView(R.id.nextPage)
    TextView nextPage;

    @Inject
    StockOutWareGoodsDetailPresenterImpl stockOutWareGoodsDetailPresenter;

    StockOutWareGoodsDetailPagerAdapter stockOutWareGoodsDetailPagerAdapter;

    private ProgressDialog progressDialog;

    private StockOutInfoDetailAgent stockOutInfoDetailAgent;

    List<StockOutInfoDetail> stockOutInfoDetailList;

    List<StockOutInfoDetail> stockOutInfoDetailListData;

    public int posFromDetailListAct = 0;

    public static final int TAG = 998;


    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_out_goods_detail;
    }

    @Override
    protected void initData() {
        setListener();
        judgeData();
    }

    private void setListener() {
        mStockOutViewPager.addOnPageChangeListener(this);
        mStockOutViewPager.setPageTransformer(true, new ViewPagerTransformer());
    }

    private void judgeData() {

        Intent intent = getIntent();
        if (intent == null) {
            return;
        } else {

            try {
                stockOutInfoDetailAgent = (StockOutInfoDetailAgent) intent
                        .getExtras().get(Constant.INTENT_TAG_FROM_STOCK_OUT_DETAIL_LIST_ACTIVITY);
                if (stockOutInfoDetailAgent != null
                        && stockOutInfoDetailAgent.stockOutInfoDetailList != null
                        && stockOutInfoDetailAgent.stockOutInfoDetailList.size() > 0) {
                    stockOutInfoDetailList = stockOutInfoDetailAgent.stockOutInfoDetailList;

                    if (stockOutInfoDetailAgent.position != null) {
                        posFromDetailListAct = stockOutInfoDetailAgent.position;
                    }
                    //初始化activity的viewpager
                    initViewPage(stockOutInfoDetailList, 0, 0, false);
                }
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void initViewPage(List<StockOutInfoDetail> stockOutInfoDetailList, int originPosition,
                             int originDataSize, boolean isRefresh) {

        if (stockOutInfoDetailList != null && stockOutInfoDetailList.size() > 0) {
            //返回列表页面重新刷新数据的DATA
            if (stockOutInfoDetailListData == null) {
                stockOutInfoDetailListData = new ArrayList<>();
                stockOutInfoDetailListData.addAll(stockOutInfoDetailList);
            } else {
                stockOutInfoDetailListData.clear();
                stockOutInfoDetailListData = stockOutInfoDetailList;
            }
            tvBnsBillNo.setText("单号  " + stockOutInfoDetailList.get(0).bnsBillNo);
            if (posFromDetailListAct == 0) {
                tvCount.setText("共 " + stockOutInfoDetailList.size() + " 条商品 , 当前第 1 条");
            } else {
                tvCount.setText("共 " + stockOutInfoDetailList.size() + " 条商品 , 当前第 " + (posFromDetailListAct + 1) + " 条");
            }

            stockOutWareGoodsDetailPagerAdapter = new StockOutWareGoodsDetailPagerAdapter(mAct, stockOutInfoDetailList);
            stockOutWareGoodsDetailPagerAdapter.setPagerAdapterCallBackListener(this);
            mStockOutViewPager.setAdapter(stockOutWareGoodsDetailPagerAdapter);
            mStockOutViewPager.setCurrentItem(posFromDetailListAct);
            //提交出库数据后刷新页面
            if (isRefresh) {
                if (stockOutWareGoodsDetailPagerAdapter.data.size() + 1 == originDataSize) {
                    mStockOutViewPager.setCurrentItem(originPosition - 1);
                } else {
                    mStockOutViewPager.setCurrentItem(originPosition);
                }
            }
        } else {
            App.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    StockOutInfoDetailAgent stockOutInfoDetailAgent = new StockOutInfoDetailAgent();
                    intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_DETAIL_LIST_ACTIVITY, stockOutInfoDetailAgent);
                    mAct.setResult(TAG, intent);
                    mAct.finish();
                }
            }, 1000);
        }
    }

    @Override
    public void dealPosition(List<StockOutInfoDetail> stockOutInfoDetailList, int position, int size) {
        if (stockOutInfoDetailList != null &&
                stockOutInfoDetailList.get(position) != null
                && stockOutInfoDetailList.get(position).id != null) {
            stockOutWareGoodsDetailPresenter.queryStockOutDetailInfo(stockOutInfoDetailList.get(position).id, size, true, position);
        }
    }

    @Override
    public void setAllocationSn(List<StockOutInfoDetail> data, int position) {
        if (data != null && data.size() > 0) {
            tvBnsBillNo.setText("单号  " + data.get(position).bnsBillNo);
        }
    }

    @Override
    public void postData(List<StockOutInfoDetail> data, int position) {
        if (data != null && data.size() > 0) {
            if(data.get(position).stockOutQty==0){
                showToastMsg("出库数量应大于0",CustomToast.WARN_STATUS);
                return;
            }
            stockOutWareGoodsDetailPresenter.postStockOutData(data, position);
        }
    }

    @Override
    public void getScanAllocationSn(int position) {
        Intent intent = new Intent(mAct, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        stockOutWareGoodsDetailPagerAdapter.curPos = position;
        tvCount.setText("共 " + stockOutWareGoodsDetailPagerAdapter.getData().size() + " 条商品 , 当前第 " + (position + 1) + " 条");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        resultData();
    }


    private void resultData() {
        Intent intent = new Intent();
        StockOutInfoDetailAgent stockOutInfoDetailAgent = new StockOutInfoDetailAgent();
        stockOutInfoDetailAgent.stockOutInfoDetailList = stockOutInfoDetailListData;
        intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_DETAIL_LIST_ACTIVITY, stockOutInfoDetailAgent);
        mAct.setResult(TAG, intent);
        mAct.finish();
    }


    @OnClick({R.id.back, R.id.userCenter, R.id.previousPage, R.id.nextPage})
    void OnClick(View view) {

        switch (view.getId()) {
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;

            case R.id.back:
                Intent intent = new Intent();
                StockOutInfoDetailAgent stockOutInfoDetailAgent = new StockOutInfoDetailAgent();
                stockOutInfoDetailAgent.stockOutInfoDetailList = stockOutInfoDetailListData;
                intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_OUT_DETAIL_LIST_ACTIVITY, stockOutInfoDetailAgent);
                mAct.setResult(TAG, intent);
                mAct.finish();
                break;

            case R.id.previousPage:
                if (stockOutWareGoodsDetailPagerAdapter.curPos > 0) {
                    mStockOutViewPager.setCurrentItem(stockOutWareGoodsDetailPagerAdapter.curPos - 1);
                }

                //向前翻页 在第一页时 无法翻页 "上一页"按钮颜色变暗
                if (stockOutWareGoodsDetailPagerAdapter.curPos == 0) {
                    previousPage.setTextColor(getResources().getColor(R.color.blue_little));
                } else {
                    previousPage.setTextColor(getResources().getColor(R.color.white));
                }
                //向前翻页 在不为最后一页时  "下一页"按钮颜色变亮
                if (stockOutWareGoodsDetailPagerAdapter.curPos + 1 != stockOutWareGoodsDetailPagerAdapter.getData().size()) {
                    nextPage.setTextColor(getResources().getColor(R.color.white));
                }

                break;

            case R.id.nextPage:
                mStockOutViewPager.setCurrentItem(stockOutWareGoodsDetailPagerAdapter.curPos + 1);

                //向后翻页 从第一页往下翻 "上一页"按钮颜色变亮
                if (stockOutWareGoodsDetailPagerAdapter.curPos > 0) {
                    previousPage.setTextColor(getResources().getColor(R.color.white));
                } else {
                    previousPage.setTextColor(getResources().getColor(R.color.blue_little));
                }

                //向后翻页 从第一页往下翻 "下一页"按钮颜色在到达最后一页的时候颜色变暗
                if (stockOutWareGoodsDetailPagerAdapter.curPos + 1 == stockOutWareGoodsDetailPagerAdapter.getData().size()) {
                    nextPage.setTextColor(getResources().getColor(R.color.blue_little));
                } else {
                    nextPage.setTextColor(getResources().getColor(R.color.white));
                }

                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                stockOutWareGoodsDetailPagerAdapter.data.get(stockOutWareGoodsDetailPagerAdapter.curPos).allocationSn = scanResult;
                stockOutWareGoodsDetailPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockOutWareGoodsDetailPresenter;
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
}
