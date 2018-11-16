package com.hclz.client.order.confirmorder.bean.products;

/**
 * Created by handsome on 16/7/27.
 */
public class NetProduct {

    public int count;
    public String pid;
    public int price;
    
    public String getPid() {
        return pid==null?"":pid;
    }

    public int getNum() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
