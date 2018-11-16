package com.hclz.client.base.util;

import android.content.Context;

/**
 * Created by hjm on 16/8/23.
 */

public class DensityUtil {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
