package com.tqmall.utils;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.tqmall.global.App;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import com.squareup.okhttp.Callback;
import com.tqmall.global.Constant;
import com.tqmall.mvp.activity.LoginActivity;
import com.tqmall.mvp.model.cache.LoginCache;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;

/**
 * Created by Jay on 16/8/30.
 */

public class H {


    public  static OkHttpClient okHttpClient;
    private static CustomerClient client;
    private static PersistentCookieStore cookieStore;

    /**
     * 封装RestAdapter
     *
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getNet(Class<T> cls) {
        if (null == client) {
            client = new CustomerClient();
        }
        if (null == cookieStore) {
            cookieStore = new PersistentCookieStore(App.getIns());
        }
        client.setCookieStore(cookieStore);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constant.serverHost)
                .setConverter(new MyGsonConverter(new Gson()))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String sharedSid = LoginCache.getIns().getSharedSid();
                        String sessionId = LoginCache.getIns().getSessionId();
                        if (!TextUtils.isEmpty(sharedSid)) {
//                            request.addHeader("Cookie", "JSESSIONID=" + sessionId + ";SHARED-SID=" + sharedSid);
                            request.addHeader("Cookie", "SHARED-SID=" + sharedSid);
                        } else {
                            Logs.d("App:{}", "sessionId is null or sharedSid is null");
                            LoginActivity.launch();
                        }

                    }
                })
                .setClient(client)
                .build();
        return restAdapter.create(cls);
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        if (null == okHttpClient) {
            createOkClient();
        }
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void post(String url, RequestBody formBody, Callback callback) {
        Request request = new Request.Builder().url(url).post(formBody).build();
        if (null == okHttpClient) {
            createOkClient();
        }
        okHttpClient.newCall(request).enqueue(callback);
    }

    private static void createOkClient() {
        okHttpClient = new OkHttpClient();
        if (null == cookieStore) {
            cookieStore = new PersistentCookieStore(App.getIns());
        }
        okHttpClient.setCookieHandler(new CookieManager(cookieStore
                ,
                CookiePolicy.ACCEPT_ALL));
    }

    public static List<HttpCookie> getCookies() {
        return cookieStore.getCookies();
    }

    public static String getCookies(String name) {
        for (HttpCookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void clearCookies() {
        if (null == cookieStore) {
            cookieStore = new PersistentCookieStore(App.getIns());
        }
        cookieStore.clear(App.getIns());
    }

}
