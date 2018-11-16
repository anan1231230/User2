package com.hclz.client.base.cart;

import org.json.JSONException;
import org.json.JSONObject;

public class CartItem {

    public String pid;
    //type id
    public String tid;
    public String name;
    public Integer price;
    public Integer pricedelta;
    public Integer num;
    public Integer sumprice;
    public Integer sumdeltaprice;
    public Integer inventory;

    public CartItem(String pid, String tid, String name, Integer price, Integer pricedelta, Integer num, Integer inventory) {
        this.pid = pid;
        this.tid = tid;
        this.name = name;
        this.price = price;
        this.pricedelta = pricedelta;
        this.num = num;
        this.inventory = inventory;
        this.sumprice = (price * num);
        this.sumdeltaprice = pricedelta * num;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pid",pid);
        obj.put("price",price);
        obj.put("count",num);
        return obj;
    }
}