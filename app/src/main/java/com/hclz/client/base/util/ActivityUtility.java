package com.hclz.client.base.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/***
 * @author jia-changyu
 */
public class ActivityUtility {
    /**
     * 标记位
     */
    private static boolean mark;

    /***
     * 私有函数，禁止实例化
     */
    private ActivityUtility() {

    }

    /***
     * 开始标记
     */
    public static void startMark() {
        mark = true;
    }

    /***
     * 结束标记
     */
    public static void stopMark() {
        mark = false;
    }

    /***
     * 是否标记
     *
     * @return mark标记
     */
    public static boolean ifMark() {
        return mark;
    }

    public static BroadcastReceiver finishActivity(final Activity activity) {
        BroadcastReceiver finishReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                activity.finish();
            }
        };
        return finishReceiver;
    }
}
