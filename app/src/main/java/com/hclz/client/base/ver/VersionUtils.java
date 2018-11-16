package com.hclz.client.base.ver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by hjm on 16/7/21.
 */

public class VersionUtils {

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.hclz.client", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("版本名称获取异常", e.getMessage());
        }
        return verName;
    }
}
