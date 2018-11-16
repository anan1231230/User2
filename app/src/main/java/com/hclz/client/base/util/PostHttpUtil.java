package com.hclz.client.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.reflect.TypeToken;
import com.hclz.client.base.application.HclzApplication;
import com.hclz.client.base.constant.ProjectConstant;
import com.hclz.client.base.constant.RequestConstant;
import com.hclz.client.base.log.SanmiLogger;
import com.hclz.client.base.ver.VersionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

public class PostHttpUtil {


    /**
     * 拼接接口request信息
     * @param requestParams 传入的值
     * @param content   接口地址
     */
    public static void prepareParams(HashMap<String, String> requestParams, String content) {
        try {
            String sigKey = "1";
            String sigValue = "meleedefaulttestsignaturekeys123";
            Map<String, String> map = HclzApplication.getData();
            HashMap<String, String> sigKeys =
                    JsonUtility.fromJson(map.get("sig_keys"), new TypeToken<HashMap<String, String>>() {
                    });
            if (sigKeys != null && !sigKeys.isEmpty()) {
                sigKey = RandomUtils.getRandomKeyFromMap(sigKeys);
                sigValue = sigKeys.get(sigKey);
            }
            long milis = System .currentTimeMillis();
            requestParams.put(RequestConstant.PUT_CONTENT.getMsg(), content);
            requestParams.put(RequestConstant.PUT_SIG_KV.getMsg(), sigKey);//暂时写死，之后需要定期从服务器取
            Mac mac = Mac.getInstance("HMACSHA256");
            String signature = CommonUtil.hmac(mac,
                    sigValue, //与上面的1对应
                    content + milis);
            requestParams.put(RequestConstant.PUT_SIGNATURE.getMsg(), signature);
            requestParams.put(RequestConstant.PUT_TIMESTAMP.getMsg(), String.valueOf(milis));
        } catch (Exception e) {
            SanmiLogger.e("", "");
        }
    }

    public static JSONObject prepareContents(Map<String, String> configMap,Context context) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ProjectConstant.APPID, configMap.get(ProjectConstant.CONFIG_APPID));
        jsonObject.put(ProjectConstant.PLATFORM, configMap.get(ProjectConstant.CONFIG_PLATFORM));
        jsonObject.put(ProjectConstant.APP_VERSION, VersionUtils.getVerName(context));
        return jsonObject;
    }

    public static boolean isnetWorkAvilable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {

            SanmiLogger.e("PostHttpUtil", "couldn't get connectivity manager");

        } else {

            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

            if (networkInfos != null) {

                for (int i = 0, count = networkInfos.length; i < count; i++) {

                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;

                    }

                }

            }

        }
        return false;
    }

    /**
     * 方法说明： 拼接基本url参数
     *
     * @param mContext
     * @param map
     * @param isKey    为true时默认带sig_kv/sig_key两个参数
     * @return
     */
    public static String getBasicUrl(Context mContext, Map<String, String> map, boolean isKey) {
        String sigKey = "1";
        String sigValue = "meleedefaulttestsignaturekeys123";
        HashMap<String, String> sigKeys =
                JsonUtility.fromJson(map.get("sig_keys"), new TypeToken<HashMap<String, String>>() {
                });
        if (sigKeys != null && !sigKeys.isEmpty()) {
            sigKey = RandomUtils.getRandomKeyFromMap(sigKeys);
            sigValue = sigKeys.get(sigKey);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mid=");
        sb.append(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_MID));
        sb.append("&sessionid=");
        sb.append(SharedPreferencesUtil.get(mContext, ProjectConstant.APP_USER_SESSIONID));
        sb.append("&appid=");
        sb.append(map.get("app_id"));
        sb.append("&platform=");
        sb.append(map.get("app_platform"));
        if (isKey) {
            sb.append("&sig_kv=");
            sb.append(sigKey);
            sb.append("&sig_key=");
            sb.append(sigValue);
        }
        return sb.toString();
    }
}
