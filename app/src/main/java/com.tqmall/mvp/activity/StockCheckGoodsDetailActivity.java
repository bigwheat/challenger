package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tqmall.R;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfo;
import com.tqmall.mvp.model.stockcheck.StockCheckDetailInfoAgent;
import com.tqmall.mvp.presenter.impl.StockCheckGoodsDetailPresenterImpl;
import com.tqmall.mvp.view.StockCheckGoodsDetailView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tqmall.mvp.activity.StockCheckDetailActivity.TAG_INTENT;

/**
 * Created by Jay on 17/2/9.
 */

public class StockCheckGoodsDetailActivity extends BaseActivity implements StockCheckGoodsDetailView{


    @BindView(R.id.tvGoodsSn)
    TextView tvGoodsSn;

    @BindView(R.id.tvAdapterModels)
    TextView tvAdapterModels;

    @BindView(R.id.tvGoodsName)
    TextView tvGoodsName;

    @BindView(R.id.tvAllocationSn)
    TextView tvAllocationSn;

    @BindView(R.id.tvGoodsFormat)
    TextView tvGoodsFormat;

    @BindView(R.id.tvOeCode)
    TextView tvOeCode;

    @BindView(R.id.tvWarehouseBillNo)
    TextView tvWarehouseBillNo;

    @BindView(R.id.etChangeQty)
    EditText etChangeQty;

    @BindView(R.id.tvConfirmStockCheck)
    TextView tvConfirmStockCheck;

    @BindView(R.id.ivSubtract)
    ImageView ivSubtract;

    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    ProgressDialog progressDialog;

    @Inject
    StockCheckGoodsDetailPresenterImpl stockCheckGoodsDetailPresenter;

    StockCheckDetailInfoAgent stockCheckDetailInfoAgent;

    List<StockCheckDetailInfo> stockCheckDetailInfo;

    StockCheckDetailInfo stockCheckDetailInfoBo=new StockCheckDetailInfo();

    StockCheckDetailInfo stockCheckDetailInfoDataPost;

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_stock_check_ware_detail;

    }

    @Override
    protected void initData() {
        judgeData();
    }

    private void judgeData(){
        Intent intent = getIntent();
        if (intent == null) {
            return;
        } else {
            try {
                stockCheckDetailInfoAgent = (StockCheckDetailInfoAgent)
                        intent.getExtras().get(Constant.INTENT_TAG_FROM_STOCK_CHECK_DETAIL_ACTIVITY);
                stockCheckDetailInfo = stockCheckDetailInfoAgent.stockCheckDetailInfos;
                if (stockCheckDetailInfoAgent != null && stockCheckDetailInfo != null
                        && stockCheckDetailInfo.size() > 0) {
                    if(stockCheckDetailInfoAgent.position != null
                            && stockCheckDetailInfo.get(stockCheckDetailInfoAgent.position)!=null ){
                        //初始化页面
                        stockCheckDetailInfoDataPost=stockCheckDetailInfo.get(stockCheckDetailInfoAgent.position);
                        initView(stockCheckDetailInfoDataPost);
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void initView(StockCheckDetailInfo stockCheckDetailInfo) {
        if(stockCheckDetailInfo!=null){
            if(stockCheckDetailInfoBo!=null){
                stockCheckDetailInfoBo=null;
                stockCheckDetailInfoBo=stockCheckDetailInfo;
            }else {
                stockCheckDetailInfoBo=stockCheckDetailInfo;
            }
            tvGoodsSn.setText(stockCheckDetailInfo.goodsSn==null ? "" : stockCheckDetailInfo.goodsSn);
            tvAdapterModels.setText(stockCheckDetailInfo.adapterModels == null ? "" : stockCheckDetailInfo.adapterModels);
            tvGoodsName.setText(stockCheckDetailInfo.goodsName == null ? "" : stockCheckDetailInfo.goodsName);
            tvAllocationSn.setText(stockCheckDetailInfo.allocationSn == null ? "" : stockCheckDetailInfo.allocationSn);
            tvGoodsFormat.setText(stockCheckDetailInfo.goodsFormat == null ? "" : stockCheckDetailInfo.goodsFormat);
            tvOeCode.setText(stockCheckDetailInfo.oeCode == null ? "" : stockCheckDetailInfo.oeCode);
            tvWarehouseBillNo.setText(stockCheckDetailInfo.warehouseBillNo == null ? "盘点单号 " : ("盘点单号 "+stockCheckDetailInfo.warehouseBillNo));
            etChangeQty.setText(stockCheckDetailInfo.checkAfterQty == null ? "" : stockCheckDetailInfo.checkAfterQty+"");
        }else {
            showToastMsg("数据获取异常,请重新刷新页面",CustomToast.WARN_STATUS);
        }
    }

    @Override
    public void onBackPressed() {
        if(stockCheckDetailInfoAgent!=null && stockCheckDetailInfoAgent.position !=null
                && stockCheckDetailInfoAgent.stockCheckDetailInfos!=null
                &&stockCheckDetailInfoAgent.stockCheckDetailInfos.size()>0){
            stockCheckDetailInfoAgent.stockCheckDetailInfos.get(stockCheckDetailInfoAgent.position).checkAfterQty
                    =stockCheckDetailInfoBo.checkAfterQty;
            Intent intent =new Intent();
            intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_CHECK_DETAIL_ACTIVITY,stockCheckDetailInfoAgent);
            setResult(TAG_INTENT,intent);
            finish();
        }else {
            finish();
        }
    }

    @OnClick({R.id.userCenter, R.id.back, R.id.tvConfirmStockCheck,R.id.ivAdd,R.id.ivSubtract})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if(stockCheckDetailInfoAgent!=null && stockCheckDetailInfoAgent.position !=null
                        && stockCheckDetailInfoAgent.stockCheckDetailInfos!=null
                        &&stockCheckDetailInfoAgent.stockCheckDetailInfos.size()>0){
                    stockCheckDetailInfoAgent.stockCheckDetailInfos.get(stockCheckDetailInfoAgent.position).checkAfterQty
                            =stockCheckDetailInfoBo.checkAfterQty;
                    Intent intent =new Intent();
                    intent.putExtra(Constant.INTENT_TAG_FROM_STOCK_CHECK_DETAIL_ACTIVITY,stockCheckDetailInfoAgent);
                    setResult(TAG_INTENT,intent);
                    finish();
                }else {
                    finish();
                }
                break;
            case R.id.userCenter:
                IntentUtils.launchClearTopWithFinish(mAct, HomeActivity.class);
                break;

            case R.id.tvConfirmStockCheck:
                String checkQtyStr=etChangeQty.getText().toString().trim();
                if(!TextUtils.isEmpty(checkQtyStr)){
                    try{
                        Integer checkAfterQty=Integer.valueOf(checkQtyStr);
                        stockCheckDetailInfoDataPost.checkAfterQty=checkAfterQty;
                        stockCheckGoodsDetailPresenter.postStockCheckData(stockCheckDetailInfoDataPost);
                    }catch (Exception e){

                    }
                }
                break;
            case R.id.ivSubtract:
                String str = etChangeQty.getText().toString().trim();
                try {
                    if (!TextUtils.isEmpty(str)) {
                        Integer changeQtyNum = Integer.valueOf(str);
                        if (changeQtyNum != null && changeQtyNum != 0) {
                            changeQtyNum--;
                            etChangeQty.setText(changeQtyNum + "");
                            etChangeQty.selectAll();
                        } else if (str.equals("")){
                            etChangeQty.setText(0+"");
                            etChangeQty.selectAll();
                        }
                    } else {
                        etChangeQty.setText(0 + "");
                        etChangeQty.selectAll();
                    }

                } catch (Exception e) {
                    etChangeQty.setText(0 + "");
                    etChangeQty.selectAll();
                }
                break;
            case R.id.ivAdd:
                String strs = etChangeQty.getText().toString().trim();
                try {
                    if (!TextUtils.isEmpty(strs)) {
                        Integer changeQtyNum = Integer.valueOf(strs);
                        if (changeQtyNum != null) {
                            changeQtyNum++;
                            etChangeQty.setText(changeQtyNum + "");
                            etChangeQty.selectAll();
                        } else if (strs.equals("")){
                            etChangeQty.setText(0+"");
                            etChangeQty.selectAll();
                        }
                    } else {
                        etChangeQty.setText(0 + "");
                        etChangeQty.selectAll();
                    }

                } catch (Exception e) {
                    etChangeQty.setText(0 + "");
                    etChangeQty.selectAll();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter=stockCheckGoodsDetailPresenter;
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
