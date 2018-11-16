package com.hclz.client.forcshop.kucunguanli.bean;

import com.hclz.client.forcshop.kucunguanli.adapter.KucunGuanliAdapter;

/**
 * Created by handsome on 16/7/7.
 */
public class TestDataItem implements KucunGuanliAdapter.KucunItem {
    int delta = 0;
    @Override
    public String getPic() {
        return "http://hclzimages.oss-cn-hangzhou.aliyuncs.com/%E6%97%A5%E5%8C%96/%E7%BE%8E%E8%8E%B1%E7%89%B9/%E7%BE%8E%E8%8E%B1%E7%89%B9%E6%B0%B4%E6%9E%9C%E8%83%B6%E7%8C%95%E7%8C%B4%E6%A1%83/suoluetu.jpg";
    }

    @Override
    public String getName() {
        return "脑袋";
    }

    @Override
    public String getNameAppend() {
        return "50g";
    }

    @Override
    public String getPid() {
        return "2323231434";
    }

    @Override
    public int getKucunliang() {
        return 5000;
    }

    @Override
    public int getDelta() {
        return delta;
    }

    @Override
    public void setDelta(int delta) {
        this.delta = delta;
    }

    @Override
    public String getReason() {
        return "哈哈哈哈";
    }

    @Override
    public void setReason(String reason) {

    }
}
