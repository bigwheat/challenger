package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.adapter.StockTransferByAllocationPageAdapter;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationAgent;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.presenter.impl.StockTransferByAllocationPresenterImpl;
import com.tqmall.mvp.view.StockTransferByAllocationView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.mvp.widget.ViewPagerTransformer;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.zxing.activity.CaptureActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 16/12/23.
 */

public class StockTransferByAllocationSnActivity extends BaseActivity implements
        StockTransferByAllocationView, ViewPager.OnPageChangeListener {


    @BindView(R.id.mViewPager)
    ViewPager mViewpager;

    @BindView(R.id.tvAllocationSn)
    TextView tvAllocationSn;

    @BindView(R.id.content)
    LinearLayout content;

    @BindView(R.id.nextPage)
    TextView nextPage;

    @BindView(R.id.previousPage)
    TextView previousPage;

    @BindView(R.id.tvCount)
    TextView tvCount;

    ProgressDialog progressDialog;

    @Inject
    StockTransferByAllocationPresenterImpl stockTransferByAllocationPresenter;


    StockTransferByAllocationPageAdapter stockTransferByAllocationPageAdapter;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_transfer_by_allocationsn;
    }

    @Override
    protected void initData() {
        mViewpager.setOnPageChangeListener(this);
        mViewpager.setPageTransformer(true, new ViewPagerTransformer());
        judgeData();
    }

    private void judgeData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        try {
            StockTransferWithAllocationAgent stockTransferWithAllocationAgent =
                    (StockTransferWithAllocationAgent) intent.getExtras().get(Constant.INTENT_TAG_FROM_TRANSFER_WITH_ALLOCATION);
            List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList =
                    stockTransferWithAllocationAgent.stockTransferWithAllocationSnBoList;
            initViewPage(stockTransferWithAllocationSnBoList, 0, false, 0);

        } catch (NullPointerException e) {

        }

    }

    @Override
    public void initViewPage(List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList
            , int originDataSize, boolean isRefresh, int originPosition) {

        if (stockTransferWithAllocationSnBoList != null && stockTransferWithAllocationSnBoList.size() > 0) {
            tvCount.setText("共 " + stockTransferWithAllocationSnBoList.size() + " 种商品 , 当前第 1 种");
            stockTransferByAllocationPageAdapter = new StockTransferByAllocationPageAdapter(mAct, stockTransferWithAllocationSnBoList);
            mViewpager.setAdapter(stockTransferByAllocationPageAdapter);
            stockTransferByAllocationPageAdapter.setAdapterDataCallBackListener(new StockTransferByAllocationPageAdapter.AdapterDataCallBack() {
                @Override
                public void setAllocationSn(List<StockTransferWithAllocationSnBo> data, int position) {
                    if (data != null && data.size() > 0) {
                        tvAllocationSn.setText("库位 " + data.get(position).allocationSn);
                    }
                }

                @Override
                public void postData(List<StockTransferWithAllocationSnBo> data, int position) {
                    if (data != null && data.size() > 0) {
                        if (data.get(position).changeQty > data.get(position).instoreQty) {
                            showToastMsg("转移数量不能大于在库数量",CustomToast.WARN_STATUS);
                        } else {
                            stockTransferByAllocationPresenter.transferStockByAllocationSn(data, position);
                        }
                    }
                }

                @Override
                public void getDestAllocationSn(int position) {
                    Intent intent = new Intent(mAct, CaptureActivity.class);
                    startActivityForResult(intent, 0);
                }
            });
            if (isRefresh) {
                if (stockTransferByAllocationPageAdapter.data.size() - 1 == originDataSize) {
                    mViewpager.setCurrentItem(originPosition - 1);
                } else {
                    mViewpager.setCurrentItem(originPosition);
                }
            }
        } else {
            showToastMsg("该库位已全部移库完成",CustomToast.SUCCESS_STATUS);
            App.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    public void dealPosition(List<StockTransferWithAllocationSnBo> dataList, int position, int size) {
        stockTransferByAllocationPresenter.queryShelvesStockByAllocationSn(dataList.get(position).allocationSn, size, true, position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        stockTransferByAllocationPageAdapter.curPos = position;
        tvCount.setText("共 " + stockTransferByAllocationPageAdapter.data.size() + " 种商品 , 当前第 " + (position + 1) + " 种");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                stockTransferByAllocationPageAdapter.data.get(stockTransferByAllocationPageAdapter.curPos).destAllocationSn = scanResult;
                stockTransferByAllocationPageAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick({R.id.back, R.id.next, R.id.nextPage, R.id.previousPage})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.next:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;

            case R.id.previousPage:
                if (stockTransferByAllocationPageAdapter.curPos > 0) {
                    mViewpager.setCurrentItem(stockTransferByAllocationPageAdapter.curPos - 1);
                }

                //向前翻页 在第一页时 无法翻页 "上一页"按钮颜色变暗
                if(stockTransferByAllocationPageAdapter.curPos==0){
                    previousPage.setTextColor(getResources().getColor(R.color.blue_little));
                }else {
                    previousPage.setTextColor(getResources().getColor(R.color.white));
                }

                //向前翻页 在不为最后一页时  "下一页"按钮颜色变亮
                if (stockTransferByAllocationPageAdapter.curPos + 1 != stockTransferByAllocationPageAdapter.getData().size()) {
                    nextPage.setTextColor(getResources().getColor(R.color.white));
                }

                break;

            case R.id.nextPage:
                mViewpager.setCurrentItem(stockTransferByAllocationPageAdapter.curPos + 1);

                //向后翻页 从第一页往下翻 "上一页"按钮颜色变亮
                if(stockTransferByAllocationPageAdapter.curPos>0){
                    previousPage.setTextColor(getResources().getColor(R.color.white));
                }else {
                    previousPage.setTextColor(getResources().getColor(R.color.blue_little));
                }

                //向后翻页 从第一页往下翻 "下一页"按钮颜色在到达最后一页的时候颜色变暗
                if (stockTransferByAllocationPageAdapter.curPos + 1 == stockTransferByAllocationPageAdapter.getData().size()) {
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
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockTransferByAllocationPresenter;
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
        hideProgress();
        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMsg(String message, int status) {
        hideProgress();
        CustomToast.showToast(mAct,message,status);
    }
}
