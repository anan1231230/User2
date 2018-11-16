package com.hclz.client.base.util;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hclz.client.base.log.LogUploadUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Http访问工具类
 *
 * @author
 */
public class JsonUtility {

    /**
     * 私有构造函数
     */
    private JsonUtility() {
        // 禁止实例化
    }

    /**
     * 将json转化为对象。
     *
     * @param json     Json字符串
     * @param classOfT 对象class
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return new Gson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
            return null;
        }
    }

    /**
     * 将字符串解析为Json对象。
     *
     * @param json Json字符串
     * @return 对象
     */
    public static JsonObject parse(String json) {
        JsonObject object = null;
        try {
            object = new JsonParser().parse(json).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
        }
        return object;
    }

    /**
     * 将字符串解析为JsonArray。
     *
     * @param json Json字符串
     * @return 对象
     */
    public static JsonArray parseArray(String json) {
        JsonArray object = null;
        try {
            object = new JsonParser().parse(json).getAsJsonArray();
        } catch (JsonSyntaxException e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
        }
        return object;
    }

    /**
     * 取得Json元素。
     *
     * @param json json字符串
     * @param key  元素key
     * @return Json元素
     */
    public static JsonElement getElement(String json, String key) {

        try {
            JsonElement element = getElement(parse(json), key);
            return element;
        } catch (Exception e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
            return null;
        }
    }

    /**
     * 取得Json元素。
     *
     * @param object json对象
     * @param key    元素key
     * @return Json元素
     */
    public static JsonElement getElement(JsonObject object, String key) {

        JsonElement element = null;
        try {
            element = parseElement(object.get(key));
        } catch (Exception e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
        }
        return element;
    }

    /**
     * Json元素转换。
     *
     * @param element 元素
     * @return 转换后Json元素
     */
    private static JsonElement parseElement(JsonElement element) {

        if (element instanceof JsonNull) {
            return null;
        }

        return element;
    }

    /**
     * 取得字符串。
     *
     * @param element json元素
     * @return 字符串
     */
    public static String getAsString(JsonElement element) {
        if (element != null) {
            String str = null;
            try {
                str = element.getAsString();
            } catch (Exception e) {
                LogUploadUtil.upload(e.getMessage());
                Log.e("fengyi.hua",e.getMessage());
            }
            return str;
        } else {
            return null;
        }
    }

    public static String getAsString(String result, String key) {
        JsonElement element = getElement(result, key);
        if (element != null) {
            String str = null;
            try {
                str = element.getAsString();
            } catch (Exception e) {
                LogUploadUtil.upload(e.getMessage());
                Log.e("fengyi.hua",e.getMessage());
            }
            return str;
        } else {
            return null;
        }
    }

    /**
     * 取得整数。
     *
     * @param element json元素
     * @return 整数
     */
    public static int getAsInt(JsonElement element) {
        if (element != null) {
            int i = -9999;
            try {
                i = element.getAsInt();
            } catch (Exception e) {
                LogUploadUtil.upload(e.getMessage());
                Log.e("fengyi.hua",e.getMessage());
            }
            return i;
        } else {
            return -999;
        }
    }

    /**
     * 取得数值。
     *
     * @param element json元素
     * @return 数值
     */
    public static double getAsDouble(JsonElement element) {
        if (element != null) {
            double d = -999.9;
            try {
                d = element.getAsDouble();
            } catch (Exception e) {
                LogUploadUtil.upload(e.getMessage());
                Log.e("fengyi.hua",e.getMessage());
            }
            return d;
        } else {
            return -999.9;
        }
    }

    /**
     * 取得布尔值。
     *
     * @param element json元素
     * @return 布尔值
     */
    public static boolean getAsBoolean(JsonElement element) {
        if (element != null) {
            boolean b = false;
            try {
                b = element.getAsBoolean();
            } catch (Exception e) {
                LogUploadUtil.upload(e.getMessage());
                Log.e("fengyi.hua",e.getMessage());
            }
            return b;
        } else {
            return false;
        }
    }

    /**
     * 将json转化为对象。
     *
     * @param element  json元素
     * @param classOfT 对象class
     * @return 对象
     */
    public static <T> T fromJson(JsonElement element, Class<T> classOfT) {

        try {
            return new Gson().fromJson(element, classOfT);
        } catch (JsonSyntaxException e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
            return null;
        }
    }

    /**
     * 将json转化为对象。
     *
     * @param json      json字符串
     * @param typeToken 类型
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {

        T t = null;
        try {
            t = new Gson().fromJson(json, typeToken.getType());
        } catch (JsonSyntaxException e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
        }
        return t;
    }

    /**
     * 将json转化为对象。
     *
     * @param element   json元素
     * @param typeToken 类型
     * @return 对象
     */
    public static <T> T fromJson(JsonElement element, TypeToken<T> typeToken) {

        T t = null;
        try {
            t = new Gson().fromJson(element, typeToken.getType());
        } catch (JsonSyntaxException e) {
            LogUploadUtil.upload(e.getMessage());
            Log.e("fengyi.hua",e.getMessage());
        }
        return t;
    }

    /**
     * jsonobject 转map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Integer> getMapForJson(String jsonStr){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            Iterator<String> keyIter= jsonObject.keys();
            String key = null;
            int value = 0;
            Map<String, Integer> valueMap = new HashMap<String, Integer>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = (int) jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
