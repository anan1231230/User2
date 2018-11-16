/**
 * 类       名:SharedPreferencesUtil
 * 主要功能:存储项目的轻量级、简单的常用配置
 */
package com.hclz.client.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.hclz.client.base.bean.MainAccount;
import com.hclz.client.base.bean.SubAccount;
import com.hclz.client.base.constant.ProjectConstant;

import java.util.List;

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


    /**
     * 保存用户信息
     *
     * @param con         环境
     * @param mainAccount 用户信息
     */
    public static void saveUserBasic(Context con, MainAccount mainAccount, List<SubAccount> subAccounts) {
        SharedPreferences sp = con.getSharedPreferences(FILENAME,
                Activity.MODE_PRIVATE);

        if (mainAccount == null) return;
        sp.edit().putString(ProjectConstant.APP_USER_MID, mainAccount.getMid() == null ? "" : mainAccount.getMid()).commit();
        sp.edit().putString(ProjectConstant.APP_USER_SESSIONID, mainAccount.getSessionid() == null ? "" : mainAccount.getSessionid()).commit();
        sp.edit().putString(ProjectConstant.APP_HX_PASSWD,mainAccount.getHxPasswd() == null?"":mainAccount.getHxPasswd()).commit();
        //subAccounts
        if (subAccounts == null) return;
        for (SubAccount subAccount : subAccounts) {
            if ("phone".equals(subAccount.getType())) {
                sp.edit().putString(ProjectConstant.APP_USER_PHONE, subAccount.getSid() == null ? "" : subAccount.getSid()).commit();

            }
        }

    }
}
