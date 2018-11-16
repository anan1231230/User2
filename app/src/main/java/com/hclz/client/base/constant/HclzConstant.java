package com.hclz.client.base.constant;

public class HclzConstant {

    private static HclzConstant mHclzConstant = null;
    public boolean isNeedRefresh = true;

    private HclzConstant() {

    }

    public static HclzConstant getInstance() {
        if (mHclzConstant == null) {
            mHclzConstant = new HclzConstant();
        }
        return mHclzConstant;
    }

}
