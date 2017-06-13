package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.tqmall.R;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.model.stockInware.StockInWareInfo;
import com.tqmall.mvp.presenter.impl.SearchStockInPresenterImpl;
import com.tqmall.mvp.view.SearchStockInView;
import com.tqmall.mvp.widget.CustomToast;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.WidgetUtils;
import com.tqmall.utils.zxing.activity.CaptureActivity;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jay on 17/1/2.
 */

public class SearchActivity extends BaseActivity implements SearchStockInView {


    @BindView(R.id.search_top_cancel)
    LinearLayout getSearch_top_cancel;

    @BindView(R.id.search_nothing)
    RelativeLayout search_nothing;

    @BindView(R.id.content)
    LinearLayout linearLayout;

    @BindView(R.id.search_image)
    ImageView search_image;

    @BindView(R.id.search_content)
    EditText searchEdit;

    @Inject
    SearchStockInPresenterImpl searchStockInPresenter;

    private ProgressDialog progressDialog;


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected int getLayoutRs() {
        return R.layout.activity_search;
    }


    @OnClick({R.id.search_top_cancel, R.id.search_image})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.search_top_cancel:
                finish();
                break;

            case R.id.search_image:
                Intent intent = new Intent(mAct, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    private void showSearchResult() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //搜索的匹配算法

            }
        });
    }

    private void setEditTextListener() {

        WidgetUtils.setEditTextInputMode(searchEdit);
        searchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                    String scanResult = searchEdit.getText().toString().trim();
                    if (!TextUtils.isEmpty(scanResult)) {
                        searchStockInPresenter.initStockInWareData(scanResult);
                        return true;
                    }
                }
                return false;
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String scanResult = bundle.getString("result");
            if (scanResult != null && scanResult.length() > 0) {
                searchEdit.setText(scanResult);
                //扫码成功后直接跳转
                searchStockInPresenter.initStockInWareData(scanResult);
            }
        }
    }

    @Override
    protected void initData() {
        setEditTextListener();
    }

    @Override
    public void returnData(StockInWareInfo stockInWareInfo) {
        if (stockInWareInfo != null) {
            //搜索成功则隐藏输入法,否则继续show
            ((InputMethodManager) searchEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            IntentUtils.launchWithObjectNoFinish(mAct, StockInWareDetailActivity.class
                    , Constant.INTENT_TAG_FROM_SEARCH_WITH_BATCH, stockInWareInfo);
        } else {
            search_nothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = searchStockInPresenter;
        mPresenter.attachView(this);
    }
}
