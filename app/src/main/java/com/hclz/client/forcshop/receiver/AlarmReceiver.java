package com.hclz.client.forcshop.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.config.SanmiConfig;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.ServerUrlConstant;
import com.hclz.client.base.exception.HttpException;
import com.hclz.client.base.handler.WeakHandler;
import com.hclz.client.base.http.HttpUtil;
import com.hclz.client.base.util.PostHttpUtil;
import com.hclz.client.base.util.SharedPreferencesUtil;
import com.hclz.client.base.util.TxtToSpeackUtil;
import com.hclz.client.forcshop.jiedanguanli.bean.BadgeBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {

    private HashMap<String, String> requestParams;
    private Map<String, String> configMap;

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        requestParams = new HashMap<String, String>();
        JSONObject content = null;
        try {
            configMap = HclzApplication.getData();
            content = PostHttpUtil.prepareContents(configMap, context);
            content.put(ProjectConstant.APP_USER_MID, SharedPreferencesUtil
                    .get(context, ProjectConstant.APP_USER_MID));
            content.put(ProjectConstant.APP_USER_SESSIONID,
                    SharedPreferencesUtil.get(context,
                            ProjectConstant.APP_USER_SESSIONID));
            content.put("cid", SharedPreferencesUtil.get(context, "cid"));
            PostHttpUtil.prepareParams(requestParams, content.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        new WeakHandler().post(runnable);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String result = HttpUtil.sendPOSTForString(ServerUrlConstant.CSHOP_ORDER_CHECK_UNCONFIRMED.getShopMethod(), requestParams, SanmiConfig.ENCODING);
                if (!result.equals(BadgeBean.getInstence().results[2])) {
                    BadgeBean.getInstence().results[2] = result;
                    JSONObject obj = new JSONObject(result);
                    String code = new JSONObject(obj.get("meta").toString()).get("code").toString();
                    if ("200".equals(code)) {
                        String count = new JSONObject(obj.get("data").toString()).get("count").toString();
                        String badge = BadgeBean.getInstence().badges[2];

                        if (TextUtils.isEmpty(badge)) {
                            badge = "0";
                        }
                        if (!count.equals(badge)) {
                            if (Integer.parseInt(count) > Integer.parseInt(badge)) {
                                TxtToSpeackUtil.getInstence().playNotification();
                            }
                            BadgeBean.getInstence().badges[2] = count;
                        }
                    }
                }
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
