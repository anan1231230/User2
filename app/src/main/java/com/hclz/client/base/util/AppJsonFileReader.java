package com.hclz.client.base.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AppJsonFileReader {
    public static String getJson(Context context, String fileName) {//从asset目录中读取文件
        String configJson = getJsonFromConfig(context, fileName);
        if (TextUtils.isEmpty(configJson)) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                AssetManager assetManager = context.getAssets();
                BufferedReader bf = new BufferedReader(new InputStreamReader(
                        assetManager.open(fileName)));
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                return null;
            }
            return stringBuilder.toString();
        } else {
            return configJson;
        }
    }

    public static String getJsonFromConfig(Context context, String fileName) {//从App缓存中读取
        StringBuilder stringBuilder = new StringBuilder();
        CacheFile cacheFile = new CacheFile(context);
        stringBuilder.append(cacheFile.readCache(fileName));
        return stringBuilder.toString();
    }

    public static Map<String, String> setData(String str) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            JSONObject object = new JSONObject(str);
            map = new HashMap<String, String>();
            map.put("app_name", object.has("app_name") ? object.getString("app_name") : "com.hclz.client");
            map.put("app_id", object.has("app_id") ? object.getString("app_id") : "1");
            map.put("app_platform", object.has("app_platform") ? object.getString("app_platform") : "android");
            map.put("app_version", object.has("app_version") ? object.getString("app_version") : "2.1.7");
            map.put("app_min_version", object.has("app_min_version") ? object.getString("app_min_version") : "2.1.7");
            map.put("release_notes", object.has("release_notes") ? object.getString("release_notes") : "[\"首次发布\"]");
            map.put("download_url", object.has("download_url") ? object.getString("download_url") : "");
            map.put("sig_keys", object.has("sig_keys") ? object.getString("sig_keys") : "{\"1\": \"meleedefaulttestsignaturekeys123\"}");
            map.put("posters", object.has("posters") ? object.getString("posters") : "[]");
            map.put("url_domains", object.has("url_domains") ? object.getString("url_domains") : "{\"user\": \"http://webapp.hclz.me:8080/hclz\",\"shop\": \"http://webapp.hclz.me:8080/hclz\",\"order\": \"http://webapp.hclz.me:8080/hclz\"," +
                    "\"assets\": \"http://webapp.hclz.me:8080/hclz\",\"socity\": \"http://webapp.hclz.me:8080/hclz\",\"product\": \"http://webapp.hclz.me:8080/hclz\"," +
                    "\"webview\": \"http://webapp.hclz.me/hclzwebview\"}");
            map.put("share_webview",object.has("share_webview") ? object.getString("share_webview") : "http://webapp.hclz.me/products/product/index.php?pid=");
            map.put("about_hclz", object.has("about_hclz") ? object.getString("about_hclz") : "{\"info\":\"\",\"features\":\"[]\"}");
            return map;
        } catch (JSONException e) {
            return getDefaultData();
        }
    }

    public static Map<String, String> getDefaultData() {
        Map<String, String> map = new HashMap<String, String>();
        map = new HashMap<String, String>();
        map.put("app_name", "com.hclz.client");
        map.put("app_id", "1");
        map.put("app_platform", "android");
        map.put("app_version", "2.1.7");
        map.put("release_notes", "[\"首次发布\"]");
        map.put("download_url", "http://hclzpublic.oss-cn-hangzhou.aliyuncs.com/apps/1/hclz_2.1.7.apk");
        map.put("sig_keys", "{\"1001\": \"v2xd7fabq8ovas1zjg13sve8bu3da705\"}");
        map.put("posters", " [" +
                "        {" +
                "            \"img\": \"http://hclzdatadev.oss-cn-hangzhou.aliyuncs.com/coupon/shouposter_coupon4.png\"," +
                "            \"webview_url\": \"http://webapp.hclz.me/hclzwebview/coupon/distribute_coupons.html\"" +
                "        }," +
                "        {" +
                "            \"img\": \"http://hclzdatadev.oss-cn-hangzhou.aliyuncs.com/coupon/shouposter_coupon4.png\"," +
                "            \"webview_url\": \"http://webapp.hclz.me/hclzwebview/coupon/distribute_coupons.html\"" +
                "        }" +
                "    ]");
        map.put("url_domains", "{\"user\": \"http://webapp.hclz.me:8080/hclz\",\"shop\": \"http://webapp.hclz.me:8080/hclz\",\"order\": \"http://webapp.hclz.me:8080/hclz\"," +
                "\"assets\": \"http://webapp.hclz.me:8080/hclz\",\"socity\": \"http://webapp.hclz.me:8080/hclz\",\"product\": \"http://webapp.hclz.me:8080/hclz\"," +
                "\"webview\": \"http://webapp.hclz.me/hclzwebview\"}");
        map.put("share_webview","http://webapp.hclz.me/products/product/index.php?pid=");
        map.put("about_hclz", "{\"info\":\"\",\"features\":\"[]\"}");
        return map;
    }
}

