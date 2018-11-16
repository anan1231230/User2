package com.hclz.client.forcshop.jiedanguanli.bean;

import com.hclz.client.forcshop.jiedanguanli.adapter.JiedanGuanliAdapter;

/**
 * Created by handsome on 16/7/7.
 */
public class TestJiedanguanli implements JiedanGuanliAdapter.JiedanItem {
    @Override
    public String getOrderId() {
        return "23425222352345";
    }

    @Override
    public String getAddress() {
        return "山东省济南市罗庄区4313街道,浪潮数字移动通信有限公司11楼29号门";
    }

    @Override
    public String getPhone() {
        return "18767573521";
    }

    @Override
    public String getYidengdai() {
        return "1小时";
    }

    @Override
    public int getPrice() {
        return 100000;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getCt() {
        return null;
    }
}
