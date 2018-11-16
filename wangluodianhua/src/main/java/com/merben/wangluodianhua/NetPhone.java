package com.merben.wangluodianhua;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.merben.wangluodianhua.constant.NetphoneConstant;
import com.merben.wangluodianhua.handler.WeakHandler;
import com.merben.wangluodianhua.listeners.NetPhoneListener;
import com.merben.wangluodianhua.util.SharedPreferencesUtil;
import com.merben.wangluodianhua.util.ToastUtil;
import com.yingtexun.entity.BackDialEntity;
import com.yingtexun.entity.RegisterEntity;
import com.yingtexun.utils.MResource;
import com.yingtexun.utils.YingTeXunInfo;

/**
 * Created by handsome on 16/5/25.
 */
public class NetPhone {

    public static NetPhoneListener mListener;

    public static void init(final Context context, NetPhoneListener listener) {
        YingTeXunInfo.registerUrl =
                context.getString(MResource.getIdByName(context, "string", "yingtexun_register_url"));
        YingTeXunInfo.callUrl =
                context.getString(MResource.getIdByName(context, "string", "yingtexun_call_url"));
        YingTeXunInfo.rechargeUrl =
                context.getString(MResource.getIdByName(context, "string", "yingtexun_recharge_url"));
        YingTeXunInfo.qureyBalanceUrl =
                context.getString(MResource.getIdByName(context, "string", "yingtexun_querybalance_url"));
        YingTeXunInfo.angentId =
                context.getString(MResource.getIdByName(context, "string", "yingtexun_agent_id"));
        mListener = listener;
        if (!TextUtils.isEmpty(SharedPreferencesUtil.get(context, NetphoneConstant.ACCOUNT_INFO_PHONE))) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        RegisterEntity entity =
                                YingTeXunInfo.register(
                                        YingTeXunInfo.registerUrl,
                                        YingTeXunInfo.angentId,
                                        SharedPreferencesUtil.get(context, NetphoneConstant.ACCOUNT_INFO_PHONE), SharedPreferencesUtil.get(context, NetphoneConstant.ACCOUNT_INFO_PASSWD));
                        //TODO 错误处理还没做
                    } catch (Exception e) {
                        ToastUtil.showToast(context, "出错:" + e.getMessage());
                    }

                }
            }).start();
        }
    }

    public static void register(final Context context, String phone, WeakHandler handler) {
        register(context, phone, "888888", handler);
    }

    private static void register(final Context context, final String phone, final String passwd, final WeakHandler handler) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    RegisterEntity entity =
                            YingTeXunInfo.register(
                                    YingTeXunInfo.registerUrl,
                                    YingTeXunInfo.angentId,
                                    phone, passwd);
                    SharedPreferencesUtil.save(context, NetphoneConstant.ACCOUNT_INFO_PHONE, phone);
                    SharedPreferencesUtil.save(context, NetphoneConstant.ACCOUNT_INFO_PASSWD, passwd);
                    SharedPreferencesUtil.save(context, NetphoneConstant.ACCOUNT_INFO_UID, entity.uid);
                    if (handler != null) {
                        handler.sendEmptyMessage(0);
                    }

                } catch (Exception e) {
                    if (handler != null) {
                        handler.sendEmptyMessage(1);
                    }
                }

            }
        }).start();
    }

    public static void charge(final Context context, final String cardnum, final String cardpasswd, final WeakHandler handler) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    RegisterEntity data =
                            YingTeXunInfo.recharge(
                                    YingTeXunInfo.rechargeUrl,
                                    YingTeXunInfo.uid,
                                    cardnum, cardpasswd);
                    Message message = new Message();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("result", data.result);
                    message.setData(bundle);
                    if (handler != null) {
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (handler != null) {
                        handler.sendEmptyMessage(1);
                    }
                }

            }
        }).start();
    }


    public static void call(final Context context, final WeakHandler handler, final String to) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    BackDialEntity data = YingTeXunInfo.backDial(YingTeXunInfo.callUrl, SharedPreferencesUtil.get(context, NetphoneConstant.ACCOUNT_INFO_UID), SharedPreferencesUtil.get(context, NetphoneConstant.ACCOUNT_INFO_PASSWD), to);
                    Message message = new Message();

                    if (data != null) {
                        message.what = 102;
                        switch (data.result) {
                            case -10:
                                message.what = -10;
                                break;
                            case -9:
                                message.what = -9;
                                break;
                            case -8:
                                message.what = -8;
                                break;
                            case -7:
                                message.what = -7;
                                break;
                            case -6:
                                message.what = -6;
                                break;
                            case -5:
                                message.what = -5;
                                break;
                            case -4:
                                message.what = -4;
                                break;
                            case -3:
                                message.what = -3;
                                break;
                            case -2:
                                message.what = -2;
                                break;
                            case -1:
                                message.what = -1;
                                break;
                            case 0:
                                message.what = 101;
                        }
                    } else {
                        message.what = 102;
                    }
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Log.e("NetPhone",
                            "出错： " + e.getMessage());
                }

            }
        }).start();
    }
}
