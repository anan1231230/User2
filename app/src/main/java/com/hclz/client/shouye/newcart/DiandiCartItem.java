package com.hclz.client.shouye.newcart;

import com.hclz.client.faxian.bean.ProductBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hjm on 16/9/26.
 */

public class DiandiCartItem {

    public String pid;
    public String name;
    public Integer price;
    public Integer num;
    public String nameAppand;
    public String imgUrl;
    public Integer inventory;
    public Integer minimal_plus;
    public Integer minimal_quantity;
    public Integer is_bookable;

    public ProductBean.ProductsBean mProduct;

    public DiandiCartItem(ProductBean.ProductsBean item,Integer num) {
        this.pid = item.pid;
        this.name = item.name;
        this.price = item.price;
        this.nameAppand = item.name_append;
        this.num = num;
        this.imgUrl = item.album_thumbnail;
        this.inventory = item.inventory;
        this.minimal_plus = item.minimal_plus;
        this.minimal_quantity = item.minimal_quantity;
        this.is_bookable = item.is_bookable;
        this.mProduct = item;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pid", pid);
        obj.put("price", price);
        obj.put("count", num);
        return obj;
    }
}
