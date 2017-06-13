package com.tqmall.mvp.activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tqmall.BuildConfig;
import com.tqmall.R;
import com.tqmall.global.App;
import com.tqmall.mvp.activity.base.BaseActivity;
import com.tqmall.mvp.activity.broadcast.NetBroadcastReceiver;
import com.tqmall.mvp.presenter.impl.HomePresenterImpl;
import com.tqmall.mvp.view.HomeView;
import com.tqmall.utils.Alert;
import com.tqmall.utils.IntentUtils;
import com.tqmall.utils.NetUtil;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;

/**
 * Created by Jay on 16/12/8.
 */

public class HomeActivity extends BaseActivity implements HomeView,
        View.OnClickListener,NetBroadcastReceiver.NetEventHandler {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_title)
    TextView main_title;

    @BindView(R.id.no_network_view)
    RelativeLayout noNetWorkView;

    @BindView(R.id.userCenter)
    ImageView userCenter;

    @BindView(R.id.content)
    LinearLayout content;

    //id名称为按钮首拼
    @BindView(R.id.ck)
    LinearLayout ck;

    @BindView(R.id.rk)
    LinearLayout rk;

    @BindView(R.id.yk)
    LinearLayout yk;

    @BindView(R.id.pd)
    LinearLayout pd;

    @BindView(R.id.cp)
    LinearLayout cp;

    @BindView(R.id.cx)
    LinearLayout cx;

    @BindView(R.id.bb)
    LinearLayout bb;

    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @Inject
    HomePresenterImpl homePresenter;

    private ProgressDialog progressDialog;

    private boolean isFirstLoad = false;

    public static int mNetWorkState;

    public NetBroadcastReceiver netBroadcastReceiver;

    public static NetBroadcastReceiver.NetEventHandler mNetEventHandler;


    @Override
    protected int getLayoutRs() {
        return R.layout.activity_home_new;

    }

    @Override
    protected void initData() {
//        ck.setVisibility(View.GONE);
//        rk.setVisibility(View.VISIBLE);//入库
//        yk.setVisibility(View.GONE);
//        pd.setVisibility(View.GONE);
//        cp.setVisibility(View.GONE);
//        cx.setVisibility(View.GONE);
//        bb.setVisibility(View.GONE);
        registerBroadcastRev();
        setListener();
        mNetWorkState = NetUtil.getNetworkState(this);
        isNetConnect();
        judgeNet();
        if(App.isDebug()){
            tvVersion.setText("D" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
        }else {
            tvVersion.setText("V" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
        }
    }

    private void registerBroadcastRev(){
        netBroadcastReceiver = new NetBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetBroadcastReceiver.NET_CHANGE_ACTION);
        registerReceiver(netBroadcastReceiver, intentFilter);
    }

    /**
     * 对网络有无连接做出处理
     */
    private void judgeNet() {

        if (isNetConnect()) {
            main_title.setText("淘汽云配");
            noNetWorkView.setVisibility(View.GONE);
        } else {
            main_title.setText("淘汽云配(未连接)");
            noNetWorkView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断网络状态
     *
     * @return
     */
    public boolean isNetConnect() {
        if (mNetWorkState == 1) {
            return true;
        } else if (mNetWorkState == 0) {
            return true;
        } else if (mNetWorkState == -1) {
            return false;

        }
        return false;
    }


    /**
     * 实时监听网络状态
     *
     * @param netWorkState
     */
    @Override
    public void onNetChange(int netWorkState) {
        mNetWorkState=netWorkState;
        switch (netWorkState) {

            case NetUtil.NETWORK_NONE:
                main_title.setText("淘汽云配(未连接)");
                noNetWorkView.setVisibility(View.VISIBLE);
                break;

            case NetUtil.NETWORK_MOBILE:
                main_title.setText("淘汽云配");
                noNetWorkView.setVisibility(View.GONE);
                break;

            case NetUtil.NETWORK_WIFI:
                main_title.setText("淘汽云配");
                noNetWorkView.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    /**
     * 设置菜单按钮显示
     * pda_stock_checkin
     * pda_stock_shelves
     * pda_stock_query
     * pda_stock_count
     * pda_stock_checkout
     * pda_stock_prepare
     *
     * @param userRole
     */
    @Override
    public void initPermissionInActivity(List<String> userRole) {

        if (userRole == null) {
            return;
        }
//        menuStr = Utils.listToString(userRole);
//        //验货
//        if (menuStr.contains("pda_stock_checkin")) {
////            ivCheckIn.setEnabled(true);
//        }
//        //上架
//        if (menuStr.contains("pda_stock_shelves")) {
////            ivShangJia.setEnabled(true);
//        }
//        //查询
//        if (menuStr.contains("pda_stock_query")) {
////            ivSearch.setEnabled(true);
//        }//盘点
//        if (menuStr.contains("pda_stock_count")) {
//            pd.setVisibility(View.VISIBLE);
//        }
//        //复核
//        if (menuStr.contains("pda_stock_checkout")) {
////            ivCheckOut.setEnabled(true);
//        }
//        //初盘
//        if (menuStr.contains("pda_stock_prepare")) {
//            cp.setEnabled(true);
//        }
        hideProgress();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userCenter:
                IntentUtils.launchNoFinish(mAct, SettingsActivity.class);
                break;
            //入库
            case R.id.rk:
                IntentUtils.launchNoFinish(mAct, StockInWareSearchActivity.class);
                break;
            //出库
            case R.id.ck:
                IntentUtils.launchNoFinish(mAct,StockOutWareSearchActivity.class);
//                showMsg("该功能正在开发 ,敬请期待");
                break;
            //移库
            case R.id.yk:
                IntentUtils.launchNoFinish(mAct, StockTransferChooseActivity.class);
                break;
            //盘点
            case R.id.pd:
                IntentUtils.launchNoFinish(mAct, StockCheckActivity.class);
                break;
            //盘库
            case R.id.cp:
                showMsg("该功能正在开发 ,敬请期待");

                break;
            //查询
            case R.id.cx:
                //IntentUtils.launchNofinish(mAct, StockInWareSearchActivity.class);
                showMsg("该功能正在开发 ,敬请期待");

                break;
            //报表
            case R.id.bb:
//                IntentUtils.launchNofinish(mAct, StockInWareDetailActivity.class);
                showMsg("该功能正在开发 ,敬请期待");

                break;

            default:
                break;
        }
    }


    private void setListener() {
        netBroadcastReceiver.setNetEventHandlerListener(this);
        userCenter.setOnClickListener(this);
        rk.setOnClickListener(this);
        ck.setOnClickListener(this);
        yk.setOnClickListener(this);
        pd.setOnClickListener(this);
        cp.setOnClickListener(this);
        cx.setOnClickListener(this);
        bb.setOnClickListener(this);
    }

    /**
     * 开启依赖注入
     */
    @Override
    protected void presenterInject() {
        mActivityComponent.inject(this);
        mPresenter = homePresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void showProgress(String msg) {
        progressDialog = Alert.progress(mAct, msg, true);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑广播
        unregisterReceiver(netBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeNet();
        if (isFirstLoad) {
            isFirstLoad = true;
        } else {
            //初始化count数目,每次进入主页重新加载用户和权限信息
            homePresenter.count = 1;
            homePresenter.loadUserInfo();
        }


    }

}
