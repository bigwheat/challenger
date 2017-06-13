package com.tqmall.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jianyun.cheng on 14-5-20.
 */
public class SignUtil {
    private static final String SIGN_KY = "1p2r3i4n5t6B7i8l9l0";
    private static final String APPID = "982401";

    public static JSONObject signPost(JSONObject jo) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> keys = jo.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                sb.append(key).append(jo.getString(key));
            }
        } catch (Exception ignored) {

        }
        String sign = new String(Hex.encodeHex(DigestUtils.sha(sb.toString() + APPID + SIGN_KY))).toUpperCase();
        try {
            jo.put("appId", APPID);
            jo.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public static String signGet(String url) {
        StringBuilder sb = new StringBuilder();
        try {
            url = url.replace("??", "?");
            String[] param = url.split("\\?")[1].split("&");
            for (String p : param) {
                sb.append(p.replace("=", ""));
            }
        } catch (Exception ignored) {

        }
        String sign = new String(Hex.encodeHex(DigestUtils.sha(sb.toString()+ APPID + SIGN_KY))).toUpperCase();
        if (url.contains("?")) {
            return url + "&appId=" + APPID + "&sign=" + sign;
        } else {
            return url + "?appId=" + APPID + "&sign=" + sign;
        }

    }

    /**
     * 签名
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String signPost(String appKey, String secret, Map<String, Object> paramMap) throws UnsupportedEncodingException {
        // 参数名排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);        // 拼接参数
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appKey);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }
        stringBuilder.append(secret);
        String encodedStr = URLEncoder.encode(stringBuilder.toString(), "UTF-8").replaceAll("\\+", "%20");        // SHA-1签名
        // For Android
        String sign = new String(Hex.encodeHex(DigestUtils.sha(encodedStr))).toUpperCase();
        return sign;
    }

    /**
     * 获取请求字符串，参数值进行UTF-8处理
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String getUrlEncodedQueryString(String appKey, String secret, Map<String, Object> paramMap) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //            String signPost = signPost(appKey, secret, paramMap);
            //            stringBuilder.append("signPost=").append(signPost);
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String queryString = stringBuilder.toString();
        return queryString;
    }

    /**
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String getPostJsonEncodedQueryString(String appKey, String secret, Map<String, Object> paramMap) {
        try {
            String sign = signPost(appKey, secret, paramMap);
            paramMap.put("signPost", sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject paramsJson = new JSONObject(paramMap);
        return paramsJson.toString();
    }

    public static String getPostFormEncodedQueryString(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paramMap.keySet()) {
            sb.append('&').append(key).append('=').append(paramMap.get(key));
        }
        String result = sb.toString();
        if (result.startsWith("&")) {
            return result.substring(1);
        } else {
            return result;
        }
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }
}
