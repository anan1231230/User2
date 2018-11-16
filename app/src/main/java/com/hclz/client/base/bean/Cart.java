package com.hclz.client.base.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart {
    //购物车类型  cshopmall/dshopmall
    private String cartType;
    //当cartType为cshopmall时，需要根据厨房id进行购物车隔离
    private String kitchenId;
    //购物车为cshopmall时类型  成品、半成品  dshopmall  半成品
    private int eatType;
    private ArrayList<Product> list;
    private HashMap<String, Integer> productNum;
    private int num;
    private int price;

    public String getCartType() {
        return cartType;
    }

    public void setCartType(String cartType) {
        this.cartType = cartType;
    }

    public String getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(String kitchenId) {
        this.kitchenId = kitchenId;
    }

    public ArrayList<Product> getList() {
        return list;
    }

    public void setList(ArrayList<Product> list) {
        this.list = list;
    }

    public boolean contains(String pid) {
        if (getList() == null || getList().size() == 0) {
            return false;
        }
        for (Product product : getList()) {
            if (product.getPid().equals(pid)) {
                return true;
            }
        }
        return false;
    }

    public int getEatType() {
        return eatType;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public HashMap<String, Integer> getProductNum() {
        return productNum;
    }

    public void setProductNum(HashMap<String, Integer> productNum) {
        this.productNum = productNum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void clear() {
        this.cartType = "";
        this.eatType = 0;
        this.list = new ArrayList<Product>();
        this.productNum = new HashMap<String, Integer>();
        this.num = 0;
        this.price = 0;
    }

}
