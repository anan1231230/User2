package com.hclz.client.base.log;

import android.content.Context;

import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.http.ExceptionHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by handsome on 16/6/25.
 */
public class LogUploadUtil {

    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static void upload(String content) {

        if (mContext == null) {
            return;
        }
        HashMap<String, String> requestParams = new HashMap<String, String>();
        final JSONObject jsonContent = new JSONObject();
        try {
            jsonContent.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(mContext, ProjectConstant.APP_USER_MID));
            jsonContent.put("app_type", "hclz");
            jsonContent.put("ios_android", "android");
            jsonContent.put("content", content);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ExceptionHttpUtil.sendPost("https://admin.hclz.me/api/error_log/", jsonContent.toString());
                }
            }).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
