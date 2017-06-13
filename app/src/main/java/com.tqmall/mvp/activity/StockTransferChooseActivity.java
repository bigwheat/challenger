package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.tqmall.R;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationAgent;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.presenter.impl.StockTransferChoosePresenterImpl;
import com.tqmall.mvp.view.StockTransferSearchView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.WidgetUtils;
import com.tqmall.utils.zxing.activity.CaptureActivity;

import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/1/5.
 */

public class StockTransferChooseActivity extends BaseActivity implements StockTransferSearchView {


    @BindView(R.id.searchByWrite)
    RelativeLayout searchByWrite;

    @BindView(R.id.searchByScanCode)
    RelativeLayout searchByScanCode;

    @Inject
    StockTransferChoosePresenterImpl stockTransferChoosePresenter;

    private final String TAG_ONE="searchByScanCode";

    private final String TAG_TWO="searchByWrite";

    private ProgressDialog progressDialog;


    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_transfer_choose;
    }


    @OnClick({R.id.searchByScanCode,R.id.searchByWrite,R.id.back,R.id.userCenter})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.searchByScanCode:
                Intent intent=new Intent(mAct,CaptureActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.searchByWrite:
                IntentUtils.launchWithObjectNoFinish(mAct,StockTransferSearchActivity.class,Constant.INTENT_TAG_FROM_CHOOSE_ACTIVITY,TAG_TWO);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct,HomeActivity.class);
            default:
                break;
        }
    }

    /**
     * 如果为批次码则请求数据传入商品移库详情页面
     * 如果为库位码则请求数据传入库位移库详情页面
     *
     * @param str
     */
    private void intentAct(String str) {

        if (WidgetUtils.BATCH.equals(WidgetUtils.scanCode(str))) {
            //为商品批次码的情况
            stockTransferChoosePresenter.queryShelvesStockByBatch(str);
        } else if(WidgetUtils.ALLOCATION.equals(WidgetUtils.scanCode(str))){
            //为库位码的情况
            stockTransferChoosePresenter.queryShelvesStockByAllocationSn(str);
        }else {
            //未知的条码,不做操作
            return;
        }
    }

    @Override
    public void returnDataByQueryInfo(List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList) {
        if (stockTransferWithAllocationSnBoList!=null&&stockTransferWithAllocationSnBoList.size()>0) {
            StockTransferWithAllocationAgent stockTransferWithAllocationAgent=new StockTransferWithAllocationAgent();
            stockTransferWithAllocationAgent.stockTransferWithAllocationSnBoList=stockTransferWithAllocationSnBoList;
            IntentUtils.launchWithObjectNoFinish(mAct,StockTransferByAllocationSnActivity.class,
                    Constant.INTENT_TAG_FROM_TRANSFER_WITH_ALLOCATION,stockTransferWithAllocationAgent);
        }
    }

    @Override
    public void returnData(StockTransferWithBatchNoBo stockTransferWithBatchNoBo) {
        if (stockTransferWithBatchNoBo!=null&&stockTransferWithBatchNoBo.pdaTransferAllocationBos!=null) {
            Intent intent=new Intent(mAct,StockTransferByBatchNoActivity.class);
            intent.putExtra(Constant.INTENT_TAG_FROM_TRANSFER_WITH_BATCHNO,stockTransferWithBatchNoBo);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                intentAct(scanResult);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter=stockTransferChoosePresenter;
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
        CustomToast.showToast(mAct,message,status);
    }
}
