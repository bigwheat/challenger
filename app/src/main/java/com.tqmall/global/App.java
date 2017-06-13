package com.tqmall.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.pgyersdk.crash.PgyCrashManager;
import com.tqmall.di.component.ApplicationComponent;
import com.tqmall.di.component.DaggerApplicationComponent;
import com.tqmall.di.module.ApplicationModule;
import com.tqmall.mvp.model.cache.LoginCache;
import com.tqmall.mvp.model.cache.UserCache;
import com.tqmall.utils.Logs;
import com.tqmall.utils.NetUtil;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 16/7/30.
 */

public class App extends Application {

    public static App app;
    public static Context mContext;
    public static SharedPreferences sharedPreferences;
    public ApplicationComponent mApplicationComponent;
    public static Handler handler;
    public static boolean  isHome;
    private List<Activity> activityList=new ArrayList<>();


    public static App getIns() {
        return app;
    }

    public App() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        handler = new Handler();
        app = this;
        isHome=false;
        PgyCrashManager.register(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        initApplicationComponent();
        initEnvironment();//初始换请求url
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 释放资源,清空缓存
     */
    private void onDestroy(){
        LoginCache.getIns().logout();
        UserCache.getIns().logout();
        MobclickAgent.onProfileSignOff();
        PgyCrashManager.unregister();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logs.d("当前需要销毁Activity的总数为:{}",String.valueOf(activityList.size()));
        for(Activity activity:activityList){
            activity.finish();
        }
        onDestroy();
        System.exit(0);
    }

    /**
     * 获得缓存对象
     *
     * @return
     */
    public SharedPreferences getUserSharedPreferences() {
        return App.getIns().getSharedPreferences(Constant.USER_CACHE, Context.MODE_PRIVATE);
    }

    public SharedPreferences getHostPreferences() {
        return App.getIns().getSharedPreferences(Constant.INIT_HOST, Context.MODE_PRIVATE);
    }

    /**
     * 初始化dagger2
     *
     * @return
     */
    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    /**
     * 初始化dagger2
     */
    private void initApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    /**
     * 初始化环境(测试 or  生产)
     */
    private void initEnvironment() {

        String serverHost = getHostPreferences().getString(Constant.HOST_CONFIG, "");
        String openIdHost = getHostPreferences().getString(Constant.OPEND_ID_HOST_CONFIG, "");
        if (TextUtils.isEmpty(serverHost) || TextUtils.isEmpty(openIdHost)) {
        if (isDebug()) {
            serverHost = Constant.SERVER_HOST_TEST;
            openIdHost = Constant.OPENID_HOST_TEST;
        } else {
            serverHost = Constant.SERVER_HOST_PRODUCT;
            openIdHost = Constant.OPENID_HOST_PRODUCT;
        }
        }
        Constant.serverHost = serverHost;
        Constant.openIdHost = openIdHost;
    }


    public static boolean isDebug() {
        try {
            ApplicationInfo info = mContext.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

}
