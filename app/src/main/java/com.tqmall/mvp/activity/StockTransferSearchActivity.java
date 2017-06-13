package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tqmall.R;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationAgent;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithAllocationSnBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.presenter.impl.StockTransferSearchPresenterImpl;
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
 * Created by Jay on 16/12/20.
 */

public class StockTransferSearchActivity extends BaseActivity implements StockTransferSearchView {

    @BindView(R.id.search_nothing)
    RelativeLayout search_nothing;

    @BindView(R.id.content)
    LinearLayout linearLayout;

    @BindView(R.id.search_content1)
    EditText searchByWrite;

    @BindView(R.id.search_content)
    EditText searchByScanCode;

    @BindView(R.id.search_image1)
    ImageView search_image1;

    @BindView(R.id.activity_search_top)
    RelativeLayout activity_search_top;

    @BindView(R.id.activity_search_top1)
    RelativeLayout activity_search_top1;

    @Inject
    StockTransferSearchPresenterImpl stockTransferPresenter;

    ProgressDialog progressDialog;

    private final String TAG_ONE="searchByScanCode";

    private final String TAG_TWO="searchByWrite";


    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_transfer;
    }

    @Override
    protected void initData() {
        judgeData();
        initViews();
    }

    private void judgeData(){
        Intent intent=getIntent();
        if(intent==null){
            return;
        }
        try{
            String tag=intent.getExtras().getString(Constant.INTENT_TAG_FROM_CHOOSE_ACTIVITY);
            if(!TextUtils.isEmpty(tag)&&tag!=null){
                if(tag.equals(TAG_ONE)){
                    //扫码
                    showView(activity_search_top);
                }else if(tag.equals(TAG_TWO)){
                    //手动输入批次码
                    showView(activity_search_top1);
                }
            }

        }catch (Exception e){

        }
    }

    private void initViews(){

        WidgetUtils.setEditTextInputMode(searchByWrite);
        searchByWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String batchNo=s.toString();
                if(batchNo.length()>0){
                    search_image1.setVisibility(View.VISIBLE);
                }else {
                    search_image1.setVisibility(View.INVISIBLE);
                }
            }
        });

        searchByWrite.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode==66 && event.getAction()==KeyEvent.ACTION_UP) {
                    String scanResult = searchByWrite.getText().toString().trim();
                    if (!TextUtils.isEmpty(scanResult)) {
                        intentAct(scanResult);
                        return true;
                    }
                }
                return false;
            }
        });

        searchByScanCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode==66 && event.getAction()==KeyEvent.ACTION_UP) {
                    String scanResult = searchByScanCode.getText().toString().trim();
                    if (!TextUtils.isEmpty(scanResult)) {
                        intentAct(scanResult);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void showView(View view){
        if(view.isShown()){
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view){
        if(!view.isShown()){
            return;
        }
        view.setVisibility(View.GONE);
    }

    @OnClick({R.id.back,R.id.userCenter,R.id.search_image1
            ,R.id.search_top_cancel,R.id.search_top_cancel1,R.id.search_image})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.search_image1:
                searchByWrite.setText("");
                break;
            case R.id.search_top_cancel:
                finish();
                break;
            case R.id.search_top_cancel1:
                finish();
                break;
            case R.id.search_image:
                Intent intent = new Intent(mAct, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct,HomeActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 扫码后跳转
     * 1.如果是批次码则跳转到商品信息页面
     * 2.如果是库位码则跳转到根据库位移库的页面
     * 3.判断条件为: 批次码的String不包含 "-"
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                searchByScanCode.setText(scanResult);
                intentAct(scanResult);
            }
        }
    }

    private boolean isBatchNo(String str) {
        if (!str.contains("-")) {
            return true;
        }
        return false;
    }

    /**
     * 如果为批次码则请求数据传入商品移库详情页面
     * 如果为库位码则请求数据传入库位移库详情页面
     *
     * @param str
     */
    private void intentAct(String str) {

        if (isBatchNo(str)) {
            //为商品批次码的情况
            stockTransferPresenter.queryShelvesStockByBatch(str);
        } else {
            //为库位码的情况
            stockTransferPresenter.queryShelvesStockByAllocationSn(str);
        }
    }

    @Override
    public void returnDataByQueryInfo(List<StockTransferWithAllocationSnBo> stockTransferWithAllocationSnBoList) {
        if (stockTransferWithAllocationSnBoList!=null&&stockTransferWithAllocationSnBoList.size()>0) {
            WidgetUtils.hideEditTextInputMode(searchByWrite);
            hideView(search_nothing);
            StockTransferWithAllocationAgent stockTransferWithAllocationAgent=new StockTransferWithAllocationAgent();
            stockTransferWithAllocationAgent.stockTransferWithAllocationSnBoList=stockTransferWithAllocationSnBoList;
            IntentUtils.launchWithObjectNoFinish(mAct,StockTransferByAllocationSnActivity.class,
                    Constant.INTENT_TAG_FROM_TRANSFER_WITH_ALLOCATION,stockTransferWithAllocationAgent);
        }else {
            search_nothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void returnData(StockTransferWithBatchNoBo stockTransferWithBatchNoBo) {
        if (stockTransferWithBatchNoBo!=null&&stockTransferWithBatchNoBo.pdaTransferAllocationBos!=null) {
            WidgetUtils.hideEditTextInputMode(searchByWrite);
            hideView(search_nothing);
            Intent intent=new Intent(mAct,StockTransferByBatchNoActivity.class);
            intent.putExtra(Constant.INTENT_TAG_FROM_TRANSFER_WITH_BATCHNO,stockTransferWithBatchNoBo);
            startActivity(intent);
        }else {
            search_nothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockTransferPresenter;
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
        Snackbar.make(linearLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMsg(String message, int status) {
        hideProgress();
        CustomToast.showToast(mAct,message,status);
    }
}
