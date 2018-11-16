package com.hclz.client.faxian.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by handsome on 1/11/16.
 */


public class HorizontalLayoutManager extends LinearLayoutManager {
    Context mContext;

    public HorizontalLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        mContext = context;
    }
}
