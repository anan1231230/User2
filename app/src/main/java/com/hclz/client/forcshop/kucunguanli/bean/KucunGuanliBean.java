package com.hclz.client.forcshop.kucunguanli.bean;

import com.hclz.client.forcshop.kucunguanli.adapter.KucunGuanliAdapter;

import java.util.List;

/**
 * Created by handsome on 2016/10/18.
 */

public class KucunGuanliBean implements KucunGuanliAdapter.KucunItem {


    /**
     * album_thumbnail : http://img.hclz.me/data%2Fe8d8ce54340d0405fd0c1439420797c5.jpg
     * albums : ["http://img.hclz.me/data%2F0c0724fd7ff5f8c4ba511e10ca5c569b.jpg","http://img.hclz.me/data%2Fcbcc50388d82d0c7fc05863dc14521cd.jpg","http://img.hclz.me/data%2F1451e7938f5630767e5f10163c33d695.jpg","http://img.hclz.me/data%2F302c032581c8e86662ce531c746c29e7.jpg","http://img.hclz.me/data%2F570c83b73705af192b2b956efe61f4e1.jpg","http://img.hclz.me/data%2F907e88e9c64a98a53977f710724fe9da.jpg","http://img.hclz.me/data%2F8d71bbd3b2efe3054bfaa431d1563a97.jpg"]
     * inventory : 1
     * is_bookable : 0
     * is_direct_selling : 1
     * leadtime : 0
     * minimal_plus : 1
     * minimal_quantity : 1
     * name : 马来西亚win2六种混合口味饼干
     * name_append : 80g*6袋
     * normal_detail : {"price_market":4079}
     * pid : 40011101
     * price : 2880
     * tags : ["食品","休闲食品","饼干"]
     * type1 : laidian
     * videos : []
     */

    private String album_thumbnail;
    private int inventory;
    private int is_bookable;
    private int is_direct_selling;
    private int leadtime;
    private int minimal_plus;
    private int minimal_quantity;
    private String name;
    private String name_append;
    /**
     * price_market : 4079
     */

    private NormalDetailBean normal_detail;
    private String pid;
    private int price;
    private String type1;
    private List<String> albums;
    private List<String> tags;
    private List<?> videos;
    private int delta = 0;
    private String reason = "";

    @Override
    public String getPic() {
        return album_thumbnail;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNameAppend() {
        return name_append;
    }

    @Override
    public String getPid() {
        return pid;
    }

    @Override
    public int getKucunliang() {
        return inventory;
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
        return reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class NormalDetailBean {
        private int price_market;

        public int getPrice_market() {
            return price_market;
        }

        public void setPrice_market(int price_market) {
            this.price_market = price_market;
        }
    }
}
