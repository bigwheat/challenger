package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tqmall.R;
import com.tqmall.enmus.BillTypeEnum;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockInware.StockInWareBillBo;
import com.tqmall.mvp.model.stockInware.StockInWareBillEntryBo;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.impl.StockInWareDetailPresenterImpl;
import com.tqmall.mvp.view.StockInWareDetailView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.callback.ResultCallback;
import com.tqmall.utils.zxing.activity.CaptureActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 16/12/14.
 */

public class StockInWareDetailActivity extends BaseActivity implements StockInWareDetailView, View.OnClickListener {

    @BindView(R.id.content)
    LinearLayout content;

    @BindView(R.id.back)
    LinearLayout back;

    @BindView(R.id.ivBnsBillType)
    ImageView ivBnsBillType;

    @BindView(R.id.tvBnsBillNo)
    TextView tvBnsBillNo;

    @BindView(R.id.tvGoodsSn)
    TextView tvGoodsSn;

    @BindView(R.id.tvAdapterModels)
    TextView tvAdapterModels;

    @BindView(R.id.tvGoodsFormat)
    TextView tvGoodsFormat;

    @BindView(R.id.tvOeCode)
    TextView tvOeCode;

    @BindView(R.id.tvInQty)
    TextView tvInQty;

    @BindView(R.id.tvOnShelveQty)
    TextView tvOnShelveQty;

    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;

    @BindView(R.id.etAllocationSn)
    EditText etAllocationSn;

    @BindView(R.id.ivSearch)
    RelativeLayout ivSearch;

    @BindView(R.id.tvRate)
    TextView tvRate;

    @BindView(R.id.etOnShelvesQty)
    EditText etOnShelvesQty;

    @BindView(R.id.tvVirtualAllocationSn)
    TextView tvVirtualAllocationSn;

    @BindView(R.id.tvConfirmStockIn)
    TextView tvConfirmStockIn;

    @BindView(R.id.tvSetRecommend)
    TextView tvSetRecommend;

    @BindView(R.id.tvOppositeName)
    TextView tvOppositeName;

    @BindView(R.id.rateView)
    RelativeLayout rateView;

    @Inject
    StockInWareDetailPresenterImpl stockInWareDetailPresenter;

    private ProgressDialog progressDialog;

    public StockInWareInfo stockInWareInfoBo;

    public StockInWareInfo stockInWareInfoFromStockInWareActivity;

    public StockInWareInfo stockInWareInfoFromStockInWareActivityWithList;

    public StockInWareInfo stockInWareInfoFromStockInWareActivityWithBatchNo;

    public StockInWareInfo stockInWareInfoForRefresh;

    public ResultCallback callback;

    public static final String STOCK_IN_ALL="STOCK_IN_ALL";

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_in_ware_detail;
    }

    @Override
    protected void initData() {
//        isShowRate();
        judgeData();
        setListener();
    }

    /**
     * 根据屏幕尺寸大小去决定是否显示rate
     *
     */
    private void isShowRate(){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =displayMetrics.heightPixels;
        if(height>480){
            rateView.setVisibility(View.VISIBLE);
        }else {
            rateView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initViews(StockInWareInfo stockInWareInfo) {
        if (stockInWareInfo == null) {
            showToastMsg("暂无该商品的上架信息",CustomToast.WARN_STATUS);
            return;
        }
        stockInWareInfoBo = stockInWareInfo;
        //应上架数量等于已上架数量时不刷新页面
        if (stockInWareInfoBo.inQty == stockInWareInfoBo.onShelvesQty) {
            showToastMsg("该商品已全部入库完成",CustomToast.SUCCESS_STATUS);
        }
        if (stockInWareInfoBo.bnsBillType == BillTypeEnum.PURCHASE_BILL.getKey()) {
            ivBnsBillType.setImageResource(R.mipmap.iv_purchase_max);
        } else if (stockInWareInfoBo.bnsBillType == BillTypeEnum.ORDER_ADJUST_BILL.getKey()) {
            ivBnsBillType.setImageResource(R.mipmap.iv_adjust_max);
        } else if (stockInWareInfoBo.bnsBillType == BillTypeEnum.SALE_RETURN_EXCHANGE_BILL.getKey()) {
            ivBnsBillType.setImageResource(R.mipmap.iv_return_max);
        }
        tvBnsBillNo.setText(stockInWareInfoBo.bnsBillNo);
        tvAdapterModels.setText(stockInWareInfoBo.adaptModels);
        tvGoodsFormat.setText(stockInWareInfoBo.goodsFormat);
        tvGoodsSn.setText(stockInWareInfoBo.goodsSn);
        tvOppositeName.setText(stockInWareInfoBo.oppositeName == null ? "" : stockInWareInfoBo.oppositeName);
        tvGoodsName.setText(stockInWareInfoBo.goodsName);
        tvOeCode.setText(stockInWareInfoBo.oeCode);
        tvOnShelveQty.setText(stockInWareInfoBo.onShelvesQty + "");
        tvInQty.setText(stockInWareInfoBo.inQty + "");
        //设置比率
        try {
            if (stockInWareInfoBo.inQty != null && stockInWareInfoBo.onShelvesQty != null) {
                int rate = stockInWareInfoBo.onShelvesQty * 100 / stockInWareInfoBo.inQty;
                tvRate.setText(rate + "%");
            }
        } catch (Exception e) {
            tvRate.setText(0 + "%");
        }
        if (!TextUtils.isEmpty(stockInWareInfoBo.virtualAllocationSn)) {
            tvVirtualAllocationSn.setText(stockInWareInfoBo.virtualAllocationSn);
            etAllocationSn.setText(stockInWareInfoBo.virtualAllocationSn);
        }
        etOnShelvesQty.setText(String.valueOf(stockInWareInfoBo.inQty - stockInWareInfo.onShelvesQty));
        etOnShelvesQty.setHint("还剩" + String.valueOf(stockInWareInfoBo.inQty - stockInWareInfo.onShelvesQty) + "个未上架");
    }

    @Override
    public void refreshViews(final StockInWareInfo stockInWareInfo, String code, String message) {
        if (code.equals(String.valueOf(BillTypeEnum.ALL_IN_WARE_END.getKey()))) {
            showToastMsg("该商品已全部入库完成", CustomToast.SUCCESS_STATUS);
            App.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent();
                    intent.putExtra(String.valueOf(StockInWareSearchActivity.INTENT_FLAG),STOCK_IN_ALL);
                    mAct.setResult(StockInWareSearchActivity.INTENT_FLAG,intent);
                    mAct.finish();
                }
            }, 1500);
        } else if (stockInWareInfo != null && code.equals(String.valueOf(BillTypeEnum.PARTIAL_IN_WARE.getKey()))) {
            showToastMsg(message, CustomToast.SUCCESS_STATUS);
            initViews(stockInWareInfo);
            stockInWareInfoForRefresh=stockInWareInfo;
        }
    }

    @Override
    public void onBackPressed() {
        if(stockInWareInfoForRefresh!=null){
            Intent intent=new Intent();
            intent.putExtra(String.valueOf(StockInWareSearchActivity.INTENT_FLAG_PART),stockInWareInfoForRefresh);
            mAct.setResult(StockInWareSearchActivity.INTENT_FLAG,intent);
            mAct.finish();
        }else {
            finish();
        }
    }

    private void setListener() {
        List<View> arrayList = new ArrayList<>();
        arrayList.add(back);
        arrayList.add(ivSearch);
        arrayList.add(tvConfirmStockIn);
        arrayList.add(tvSetRecommend);
        for (View view : arrayList) {
            view.setOnClickListener(this);
        }
    }

    /**
     * 初始化数据
     * <p>
     * 1.获取Intent对象,判断是否为空
     * 2.判断是否有批次码从入库作业的页面传入
     * 2.1 如果有则根据批次码查询商品入库信息
     * 2.2 加载数据
     * 3.判断是否有从入库作业的页面传入的商品list,如果有则初始化加载
     * 4.判断是否有从入库单详情的页面(根据业务单号获取单据下所有未入库商品【随机返回一条】)传入的商品list,如果有则初始化加载
     */
    private void judgeData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        } else {
            try {
                stockInWareInfoFromStockInWareActivity = (StockInWareInfo) intent.getExtras().get(Constant.INTENT_TAG_FROM_STOCK_IN);
                stockInWareInfoFromStockInWareActivityWithBatchNo = (StockInWareInfo) intent.getExtras().get(Constant.INTENT_TAG_FROM_SEARCH_WITH_BATCH);
                stockInWareInfoFromStockInWareActivityWithList = (StockInWareInfo) intent.getExtras().get(Constant.INTENT_TAG_FROM_SEARCH_WITH_LIST);
                if (stockInWareInfoFromStockInWareActivity != null) {
                    initViews(stockInWareInfoFromStockInWareActivity);
                } else if (stockInWareInfoFromStockInWareActivityWithBatchNo != null) {
                    initViews(stockInWareInfoFromStockInWareActivityWithBatchNo);
                } else if (stockInWareInfoFromStockInWareActivityWithList != null) {
                    initViews(stockInWareInfoFromStockInWareActivityWithList);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上架
     */
    private void postData() {
        String onShelvesQty = etOnShelvesQty.getText().toString();
        String allocationSn = etAllocationSn.getText().toString();
        Integer onShelvesQtyInteger = null;
        if (!TextUtils.isEmpty(onShelvesQty) && !TextUtils.isEmpty(allocationSn)) {
            try {
                onShelvesQtyInteger = Integer.valueOf(onShelvesQty);
            } catch (Exception e) {
                showToastMsg("请重新输入入库数量",CustomToast.WARN_STATUS);
                etOnShelvesQty.setText("");
                return;
            }
            if (stockInWareInfoBo == null || stockInWareInfoBo.inQty == null) {
                return;
            }
            if (onShelvesQtyInteger > stockInWareInfoBo.inQty - stockInWareInfoBo.onShelvesQty) {
                showToastMsg("入库数量不能大于剩余应入库数量",CustomToast.WARN_STATUS);
                etOnShelvesQty.setText("");
                return;
            }
            StockInWareBillBo stockInWareBillBo = new StockInWareBillBo();
            stockInWareBillBo.id = stockInWareInfoBo.warehouseBillId;
            List<StockInWareBillEntryBo> stockInWareBillEntryBoList = new ArrayList<>();
            StockInWareBillEntryBo stockInWareBillEntryBo = new StockInWareBillEntryBo();
            stockInWareBillEntryBo.onShelvesQty = onShelvesQtyInteger;
            stockInWareBillEntryBo.allocationSn = allocationSn;
            stockInWareBillEntryBo.id = stockInWareInfoBo.warehouseEntryId;
            stockInWareBillEntryBo.goodsId = stockInWareInfoBo.goodsId;
            stockInWareBillEntryBo.batchNo = stockInWareInfoBo.batchNo;
            stockInWareBillEntryBoList.add(stockInWareBillEntryBo);
            stockInWareBillBo.inWareBillEntryBoList = stockInWareBillEntryBoList;
            stockInWareDetailPresenter.postData(stockInWareBillBo);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if(stockInWareInfoForRefresh!=null){
                    Intent intent=new Intent();
                    intent.putExtra(String.valueOf(StockInWareSearchActivity.INTENT_FLAG_PART),stockInWareInfoForRefresh);
                    mAct.setResult(StockInWareSearchActivity.INTENT_FLAG,intent);
                    mAct.finish();
                }else {
                    finish();
                }
                break;

            case R.id.ivSearch:
                Intent intent = new Intent(mAct, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.tvConfirmStockIn:
                postData();
                break;

            case R.id.tvSetRecommend:
                if (stockInWareInfoBo != null) {
                    etAllocationSn.setText(stockInWareInfoBo.virtualAllocationSn);
                }
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.userCenter)
    void home() {
        IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
    }

    @OnClick(R.id.ac_layout1)
    void intentIntoStockInWareAct() {
        IntentUtils.launchNoFinish(mAct, StockInWareActivity.class, stockInWareInfoBo.bnsBillNo, Constant.INTENT_TAG_FROM_STOCK_IN_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                etAllocationSn.setText(scanResult);
            }
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = stockInWareDetailPresenter;
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
        CustomToast.showToast(mAct, message, status);
    }
}
