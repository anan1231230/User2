/**
 * 类       名:SharedPreferencesUtil
 * 主要功能:存储项目的轻量级、简单的常用配置
 */
package com.merben.wangluodianhua.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String FILENAME = "hclz_sp";

    /**
     * 保存用户信息
     *
     * @param con   环境
     * @param key   key
     * @param value value
     */
    public static void save(Context con, String key, String value) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME,
                Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 获取信息
     *
     * @param con 环境
     * @param key key
     * @return value
     */
    public static String get(Context con, String key) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME,
                Activity.MODE_PRIVATE);
        return sp.getString(key, null);
    }


    /**
     * 保存用户信息
     *
     * @param con   环境
     * @param key   key
     * @param value value
     */
    public static void saveBoolean(Context con, String key, boolean value) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME,
                Activity.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取信息
     *
     * @param con 环境
     * @param key key
     * @return value
     */
    public static boolean getBoolean(Context con, String key) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME,
                Activity.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }
}
