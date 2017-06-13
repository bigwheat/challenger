package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoBo;
import com.tqmall.mvp.model.stockTransfer.StockTransferWithBatchNoEntryBo;
import com.tqmall.mvp.presenter.impl.StockTransferByBatchNoPresenterImpl;
import com.tqmall.mvp.view.StockTransferByBatchNoView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.Logs;
import com.tqmall.utils.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 16/12/20.
 */

public class StockTransferByBatchNoActivity extends BaseActivity implements StockTransferByBatchNoView {


    @BindView(R.id.content)
    RelativeLayout content;

    @BindView(R.id.itemView)
    LinearLayout itemView;

    @BindView(R.id.etAllocationSn)
    EditText etAllocationSn;

    @BindView(R.id.tvGoodsSn)
    TextView tvGoodsSn;

    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;

    @BindView(R.id.tvAdapterModels)
    TextView tvAdapterModels;

    @BindView(R.id.tvGoodsFormat)
    TextView tvGoodsFormat;

    @BindView(R.id.tvInQty)
    TextView tvInQty;

    @BindView(R.id.tvbatchNo)
    TextView tvbatchNo;

    @BindView(R.id.ivSearch)
    RelativeLayout ivSearch;

    @Inject
    StockTransferByBatchNoPresenterImpl stockTransferByBatchNoPresenter;

    private ProgressDialog progressDialog;

    private StockTransferWithBatchNoBo stockTransferWithBatchNoBo;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_transfer_by_batchno_new;
    }

    @Override
    protected void initData() {
        init();
        judgeData();
    }

    private void init() {

    }

    private void judgeData() {
        Intent intent = this.getIntent();
        if (intent == null) {
            return;
        }
        try {
            stockTransferWithBatchNoBo
                    = (StockTransferWithBatchNoBo) intent.getExtras().get(Constant.INTENT_TAG_FROM_TRANSFER_WITH_BATCHNO);
            if (stockTransferWithBatchNoBo != null &&
                    stockTransferWithBatchNoBo.pdaTransferAllocationBos.size() > 0) {
                initHead(stockTransferWithBatchNoBo);
                initListView(stockTransferWithBatchNoBo.pdaTransferAllocationBos);
            }

        } catch (NullPointerException e) {

        }
    }

    /**
     * 初始化商品信息
     */
    private void initHead(StockTransferWithBatchNoBo stockTransferWithBatchNoBo) {
        tvbatchNo.setText(stockTransferWithBatchNoBo.batchNo);
        tvGoodsSn.setText(stockTransferWithBatchNoBo.goodsSn);
        tvGoodsName.setText(stockTransferWithBatchNoBo.goodsName);
        tvAdapterModels.setText(stockTransferWithBatchNoBo.adapterModels);
        tvGoodsFormat.setText(stockTransferWithBatchNoBo.goodsFormat);
        tvInQty.setText(String.valueOf(stockTransferWithBatchNoBo.sumInstoreQty));
    }

    @Override
    public void refreshViews(StockTransferWithBatchNoBo stockTransferWithBatchNoBo) {
        if (stockTransferWithBatchNoBo != null &&
                stockTransferWithBatchNoBo.pdaTransferAllocationBos.size() > 0) {
            this.stockTransferWithBatchNoBo = stockTransferWithBatchNoBo;
            initHead(stockTransferWithBatchNoBo);
            initListView(stockTransferWithBatchNoBo.pdaTransferAllocationBos);
        } else {
            App.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1500);
        }
    }

    @Override
    public void initListView(List<StockTransferWithBatchNoEntryBo> stockTransferWithBatchNoEntryBoList) {
        itemView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mAct);
        for (final StockTransferWithBatchNoEntryBo stockTransferWithBatchNoEntryBo : stockTransferWithBatchNoEntryBoList) {
            View viewItem = inflater.inflate(R.layout.activity_stock_transfer_by_batchno_item, null);
            TextView tvVirtualAllocationSn = (TextView) viewItem.findViewById(R.id.tvVirtualAllocationSn);
            TextView tvNumber = (TextView) viewItem.findViewById(R.id.tvNumber);
            final EditText etTransferNumber = (EditText) viewItem.findViewById(R.id.etTransferNumber);
            if (stockTransferWithBatchNoEntryBo.allocationSn != null) {
                tvVirtualAllocationSn.setText(stockTransferWithBatchNoEntryBo.allocationSn);
            }
            if (stockTransferWithBatchNoEntryBo.instoreQty != null) {
                tvNumber.setText(String.valueOf(stockTransferWithBatchNoEntryBo.instoreQty));
            }
            if ((stockTransferWithBatchNoEntryBo.changeQty != null) && stockTransferWithBatchNoEntryBo.changeQty > 0) {
                etTransferNumber.setText(String.valueOf(stockTransferWithBatchNoEntryBo.changeQty));
            }
            etTransferNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etTransferNumber.setText(etTransferNumber.getText().toString());// 添加这句后实现效果
                    etTransferNumber.selectAll();
                }
            });
            etTransferNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        String str = s.toString();
                        if (!TextUtils.isEmpty(str)) {
                            Integer etTransferNumber = Integer.valueOf(str);
                            stockTransferWithBatchNoEntryBo.changeQty = etTransferNumber;
                        }
                    } catch (Exception e) {

                    }
                }
            });
            itemView.addView(viewItem);
        }
        Logs.d("READY_POST_DATA", stockTransferWithBatchNoEntryBoList.toString());
    }

    @Override
    public void isFinishAct(Boolean isNeed, String message, String batchNo) {
        if (isNeed) {
            showToastMsg(message, CustomToast.SUCCESS_STATUS);
            stockTransferByBatchNoPresenter.queryShelvesStockByBatch(batchNo);
        } else {
            showToastMsg(message, CustomToast.WARN_STATUS);
            stockTransferByBatchNoPresenter.queryShelvesStockByBatch(batchNo);
        }
    }

    /**
     * 确认移库
     */
    private void postData() {
        String etAllocationSnStr = etAllocationSn.getText().toString().trim();
        List<StockTransferWithBatchNoEntryBo> stockTransferWithBatchNoEntryBoLists = stockTransferWithBatchNoBo.pdaTransferAllocationBos;
//        int count=0;
//        for(StockTransferWithBatchNoEntryBo bo:stockTransferWithBatchNoEntryBoLists){
//            if(bo.changeQty==null){
//                count++;
//                continue;
//            }
//            if(bo.changeQty==0){
//                count++;
//            }
//        }
//        if(count==stockTransferWithBatchNoEntryBoLists.size()){
//            showToastMsg("转移数量不能全部为0或者空",CustomToast.WARN_STATUS);
//            return;
//        }
//


//        int size = stockTransferWithBatchNoEntryBoLists.size();
//        for (int i = 0; i < size; i++) {
//            StockTransferWithBatchNoEntryBo stockTransferWithBatchNoEntryBo = stockTransferWithBatchNoEntryBoLists.get(i);
//            if (stockTransferWithBatchNoEntryBo.changeQty == null || stockTransferWithBatchNoEntryBo.changeQty == 0) {
//                stockTransferWithBatchNoEntryBoLists.remove(i);
//            }
//        }
        List<StockTransferWithBatchNoEntryBo> dataList=new ArrayList<>();
        if(dataList!=null && dataList.size()>0){
            dataList.clear();
        }
        dataList.addAll(stockTransferWithBatchNoEntryBoLists);


        Iterator iterator = dataList.iterator();
        while (iterator.hasNext()) {
            StockTransferWithBatchNoEntryBo stockTransferWithBatchNoEntryBo
                    = (StockTransferWithBatchNoEntryBo) iterator.next();
            if (stockTransferWithBatchNoEntryBo.changeQty == null || stockTransferWithBatchNoEntryBo.changeQty == 0) {
                iterator.remove();
            }

        }
        if (dataList.size() == 0) {
            showToastMsg("转移数量不能全部为0或者空", CustomToast.WARN_STATUS);
            return;
        }

        if (!TextUtils.isEmpty(etAllocationSnStr)) {
            stockTransferWithBatchNoBo.destAllocationSn = etAllocationSnStr;
            stockTransferWithBatchNoBo.pdaTransferAllocationBos=dataList;
            stockTransferByBatchNoPresenter.transferStockByBatchNo(stockTransferWithBatchNoBo);
        } else {
            showToastMsg("转移后库位不能为空", CustomToast.WARN_STATUS);
        }

    }

    @OnClick({R.id.userCenter, R.id.back, R.id.tvCommit, R.id.ivSearch})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tvCommit:
                postData();
                break;
            case R.id.ivSearch:
                Intent intent = new Intent(mAct, CaptureActivity.class);
                startActivityForResult(intent, 0);
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
                etAllocationSn.setText(scanResult);
            }
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockTransferByBatchNoPresenter;
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
