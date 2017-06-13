package com.tqmall.mvp.model.cache;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.tqmall.global.App;
import com.tqmall.global.Constant;
import com.tqmall.utils.Des;
import com.tqmall.utils.H;
import com.tqmall.utils.Logs;
import com.tqmall.utils.MD5;
import com.tqmall.utils.callback.ResultCallback;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay on 16/12/5.
 */

public class LoginCache {

    public static LoginCache loginCache = new LoginCache();
    public SharedPreferences sharedPreferences;
    private String sharedSid;
    private String sessionId;
    private ResultCallback callback;
    private String userName;
    private String userPassword;


    public static LoginCache getIns() {
        return loginCache;
    }


    private LoginCache() {
        sharedPreferences = App.getIns().getUserSharedPreferences();
    }

    public String getSharedSid() {
        return sharedSid;
    }

    public String getSessionId() {
        return sessionId;
    }

    /**
     * 缓存账号和密码
     *
     * @param userName
     * @param userPassword
     */
    public void setLoginInfo(String userName, String userPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.USER_NAME, userName);
        if (!TextUtils.isEmpty(userPassword)) {
            editor.putString(Constant.USER_PASSWORD, Des.encryptDES(userPassword, Constant.DES_CODE));
        } else {
            editor.putString(Constant.USER_PASSWORD, "");
        }
        editor.commit();
    }

    /**
     * 获取缓存的账号和密码
     *
     * @return
     */
    public Map<String, String> getLoginInfo() {
        try {
            String userName = sharedPreferences.getString(Constant.USER_NAME, null);
            String userPassword = sharedPreferences.getString(Constant.USER_PASSWORD, null);
            Map<String, String> map = new HashMap<>();
            if (!TextUtils.isEmpty(userName)) {
                map.put(Constant.USER_NAME, userName);
                map.put(Constant.USER_PASSWORD, Des.decryptDES(userPassword, Constant.DES_CODE));
            }
            return map;
        } catch (Exception e) {
            Logs.e("解码异常:{}", e.getMessage());
            return null;
        }
    }


    public void login(String userName, String userPassword, boolean remember, final ResultCallback callback) {
        this.callback = callback;
        this.userName = userName;
        this.userPassword = userPassword;
        if (remember) {
            setLoginInfo(userName, userPassword);
        } else {
            setLoginInfo("", "");
        }
        H.get(Constant.serverHost + "/",
                new com.squareup.okhttp.Callback() {
                    @Override
                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                        Logs.d("当前登录的url为:{}", response.request().url().toString());
                        String FullUrl = response.request().url().toString();
                        if (FullUrl.contains(Constant.verifyUrl)) {
                            sessionId = H.getCookies("JSESSIONID");
                            sharedSid = H.getCookies("SHARED-SID");
                            if (!TextUtils.isEmpty(sharedSid)) {
                                Logs.d("openid SHARED-SID:", sharedSid);
                                callback.success();
                            } else {
                                openidLogin();
                            }
                        } else {
                            sessionId = null;
                            openidLogin();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Logs.e("登录失败[login()]",e.toString());
                        loginFailed("登录失败,请检查网络连接,若网络连接正常,请联系系统管理员");
                    }
                });
    }

    private void openidLogin() {

        H.get(Constant.serverHost + "/loginAction", new
                com.squareup.okhttp.Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Logs.e("登录失败[openidLogin()]",e.toString());
                        loginFailed("登录失败,请检查网络连接,若网络连接正常,请联系系统管理员");
                    }

                    @Override
                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                        next();
                    }
                });
    }

    private void loginFailed(String info) {
        Logs.d("登录失败,失败信息为:{}", info);
        LoginCache.getIns().logout();
        callback.error(info);
    }

    private void next() {
        String ePass = MD5.getMD5(userPassword);
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", userName)
                .add("password", ePass)
                .build();
        H.post(Constant.serverHost + "/loginAction", formBody,
                new com.squareup.okhttp.Callback() {
                    @Override
                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                        Logs.d("next()_url:{}", response.request().url().toString());
                        check();
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Logs.e("登录[next()]",e.toString());
                        loginFailed("登录失败,请检查网络连接,若网络连接正常,请联系系统管理员");
                    }
                });
    }

    private void check() {
        H.get(Constant.serverHost + "/",
                new com.squareup.okhttp.Callback() {
                    @Override
                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                        Logs.d("check()_url:{}", response.request().url().toString());
                        if (response.request().url().toString().contains(Constant.verifyUrl)) {
                            //callback.loginSuccess();
                            sessionId = H.getCookies("JSESSIONID");
                            sharedSid = H.getCookies("SHARED-SID");
                            if (!TextUtils.isEmpty(sharedSid)) {
                                callback.success();
                            } else {
                                loginFailed("  cookie为空");
                            }

                        } else {
                            sessionId = null;
                            loginFailed("  登录失败 , 请检查用户名密码是否有误");
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Logs.e("登录失败[check()]",e.toString());
                        loginFailed("登录失败,请检查网络连接,若网络连接正常,请联系系统管理员");
                    }
                });
    }

    public void logout() {
        setLoginInfo("", "");
        H.clearCookies();
        loginCache = new LoginCache();
    }
}
