package com.tqmall.utils;

import com.squareup.okhttp.OkHttpClient;
import com.tqmall.mvp.model.cache.UserCache;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by jianyuncheng on 15/4/30.
 */
public class CustomerClient extends OkClient {

    private OkHttpClient client;

    private PersistentCookieStore cookieStore;

    public CustomerClient() {
    }

    public CustomerClient(OkHttpClient client) {
        super(client);
        this.client = client;
    }

    public void setCookieStore(PersistentCookieStore cookieStore) {
        this.cookieStore = cookieStore;
        if (null != client) {
            client.setCookieHandler(new CookieManager(cookieStore
                    ,
                    CookiePolicy.ACCEPT_ALL));
        }
    }
    @Override
    public Response execute(Request request) throws IOException {
        List<Header> headers = new ArrayList<>();
        headers.addAll(request.getHeaders());
        Request updated;
        String newUrl = request.getUrl();
        if ("GET".equals(request.getMethod())) {
//            newUrl = addGetParam(request.getUrl());
            updated = new Request(request.getMethod(), newUrl, headers, request.getBody());
        } else {
            updated = new Request(request.getMethod(), request.getUrl(), headers, request.getBody());
        }
        Logs.d("api request:{}" ,newUrl);
        return super.execute(updated);
    }

    private String addGetParam(String url) {
        Integer wareHouseId = UserCache.getIns().getWareHouseId();
        Long userId = UserCache.getIns().getUserId();
        if (url.contains("?")) {
            if (!url.contains("warehouseId") && wareHouseId != null) {
                url += "&warehouseId=" + wareHouseId;
            }
            if (!url.contains("userId") && userId != null) {
                url += "&userId=" + userId;
            }
        } else {
            url += "?" +
                    (wareHouseId == null ? "" : "warehouseId=" + wareHouseId) +
                    (userId == null ? "" : "&userId=" + userId);
        }
        return url;
    }

}
